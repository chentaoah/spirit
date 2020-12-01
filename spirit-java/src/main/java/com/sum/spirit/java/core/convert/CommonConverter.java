package com.sum.spirit.java.core.convert;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sum.spirit.java.api.ElementConverter;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.utils.Maps;

@Component
@Order(-100)
public class CommonConverter implements ElementConverter {

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.statement);
	}

	public void convertStmt(IClass clazz, Statement statement) {

		for (Token token : statement.tokens) {

			if (token.canSplit()) {
				convertStmt(clazz, token.getValue());
			}

			if (token.isArrayInit()) {// String[10] => new String[10]
				Statement subStatement = token.getValue();
				subStatement.addToken(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (token.isTypeInit()) {// User() => new User()
				Statement subStatement = token.getValue();
				subStatement.addToken(0, new Token(TokenTypeEnum.KEYWORD, "new"));

			} else if (token.isList()) {// ["value"] => Lists.newArrayList("value");
				Statement subStatement = token.getValue();
				subStatement.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Lists.newArrayList("));
				subStatement.setToken(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Lists.class.getName());

			} else if (token.isMap()) {// {"key":"value"} => MapBuilder.of("key","value");
				Statement subStatement = token.getValue();
				for (Token subToken : subStatement.tokens) {
					if (subToken.isSeparator() && ":".equals(subToken.toString())) {
						subToken.value = ",";
					}
				}
				subStatement.setToken(0, new Token(TokenTypeEnum.CUSTOM_PREFIX, "Maps.of("));
				subStatement.setToken(subStatement.size() - 1, new Token(TokenTypeEnum.CUSTOM_SUFFIX, ")"));
				clazz.addImport(Maps.class.getName());
			}
		}
	}

}
