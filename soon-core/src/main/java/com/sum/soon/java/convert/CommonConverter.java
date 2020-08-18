package com.sum.soon.java.convert;

import com.sum.pisces.api.annotation.Order;
import com.sum.soon.api.convert.ElementConverter;
import com.sum.soon.lib.Collection;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.common.Constants;
import com.sum.soon.pojo.element.Element;
import com.sum.soon.pojo.element.Statement;
import com.sum.soon.pojo.element.Token;

@Order(-100)
public class CommonConverter implements ElementConverter {

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.stmt);
	}

	public void convertStmt(IClass clazz, Statement stmt) {

		for (Token token : stmt.getTokens()) {

			if (token.canSplit())
				convertStmt(clazz, token.getValue());

			if (token.isArrayInit()) {// String[10] => new String[10]
				Statement subStmt = token.getValue();
				subStmt.addToken(0, new Token(Constants.KEYWORD_TOKEN, "new"));

			} else if (token.isTypeInit()) {// User() => new User()
				Statement subStmt = token.getValue();
				subStmt.addToken(0, new Token(Constants.KEYWORD_TOKEN, "new"));

			} else if (token.isList()) {// ["value"] => Collection.newArrayList("value");
				Statement subStmt = token.getValue();
				subStmt.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "Collection.newArrayList("));
				subStmt.setToken(subStmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
				clazz.addImport(Collection.class.getName());

			} else if (token.isMap()) {// {"key":"value"} => Collection.newHashMap("key","value");
				Statement subStmt = token.getValue();
				for (Token subToken : subStmt.tokens) {
					if (subToken.isSeparator() && ":".equals(subToken.toString()))
						subToken.value = ",";
				}
				subStmt.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "Collection.newHashMap("));
				subStmt.setToken(subStmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
				clazz.addImport(Collection.class.getName());
			}
		}
	}

}
