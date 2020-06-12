package com.sum.shy.java.converter;

import com.sum.shy.lib.Collection;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.common.Constants;
import com.sum.shy.pojo.element.Statement;
import com.sum.shy.pojo.element.Token;

public class CommonConverter {

	public static void convertStmt(IClass clazz, Statement stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);

			if (token.canSplit())
				convertStmt(clazz, token.getValue());

			if (token.isArrayInit()) {
				Statement subStmt = token.getValue();
				subStmt.addToken(0, new Token(Constants.KEYWORD_TOKEN, "new"));

			} else if (token.isTypeInit()) {// 在所有的构造函数前面都加个new
				Statement subStmt = token.getValue();
				subStmt.addToken(0, new Token(Constants.KEYWORD_TOKEN, "new"));

			} else if (token.isList()) {// 将所有的array和map都转换成方法调用
				Statement subStmt = token.getValue();
				subStmt.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "Collection.newArrayList("));
				subStmt.setToken(subStmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
				clazz.addImport(Collection.class.getName());// 添加依赖

			} else if (token.isMap()) {
				Statement subStmt = token.getValue();
				for (Token subToken : subStmt.tokens) {// 将map里面的冒号分隔符,转换成逗号分隔
					if (subToken.isSeparator() && ":".equals(subToken.toString()))
						subToken.value = ",";
				}
				subStmt.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "Collection.newHashMap("));
				subStmt.setToken(subStmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
				clazz.addImport(Collection.class.getName());// 添加依赖

			}
		}
	}
}
