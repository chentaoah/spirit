package com.sum.shy.java.converter;

import com.sum.shy.clazz.IClass;
import com.sum.shy.common.Constants;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;
import com.sum.shy.lib.Collection;

public class CommonConverter {

	public static void convertStmt(IClass clazz, Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);

			if (token.canVisit())
				convertStmt(clazz, token.getStmt());

			if (token.isArrayInit()) {
				Stmt subStmt = token.getStmt();
				subStmt.addToken(0, new Token(Constants.KEYWORD_TOKEN, "new"));

			} else if (token.isTypeInit()) {// 在所有的构造函数前面都加个new
				Stmt subStmt = token.getStmt();
				subStmt.addToken(0, new Token(Constants.KEYWORD_TOKEN, "new"));

			} else if (token.isList()) {// 将所有的array和map都转换成方法调用
				Stmt subStmt = token.getStmt();
				subStmt.setToken(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "Collection.newArrayList("));
				subStmt.setToken(subStmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
				clazz.addImport(Collection.class.getName());// 添加依赖

			} else if (token.isMap()) {
				Stmt subStmt = token.getStmt();
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
