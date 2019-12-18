package com.sum.shy.java.convert;

import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.api.Converter;
import com.sum.shy.lib.Collection;

public class DefaultConverter implements Converter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		return convertStmt(clazz, stmt);
	}

	public static Stmt convertStmt(CtClass clazz, Stmt stmt) {
		// 将语句进行一定的转换
		stmt = convertSubStmt(clazz, stmt);
		// 这个添加的后缀,使得后面不会加上空格
		stmt.tokens.add(new Token(Constants.SUFFIX_TOKEN, ";", null));

		return stmt;

	}

	public static Stmt convertSubStmt(CtClass clazz, Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);

			if (token.isArray()) {// 将所有的array和map都转换成方法调用
				Stmt subStmt = (Stmt) token.value;
				convertSubStmt(clazz, subStmt);
				subStmt.getToken(0).value = "Collection.newArrayList(";
				subStmt.getToken(subStmt.size() - 1).value = ")";

				// 添加依赖
				clazz.addImport(Collection.class.getName());

			} else if (token.isMap()) {
				Stmt subStmt = (Stmt) token.value;
				convertSubStmt(clazz, subStmt);
				for (Token subToken : subStmt.tokens) {// 将map里面的冒号分隔符,转换成逗号分隔
					if (subToken.isSeparator() && ":".equals(subToken.value)) {
						subToken.value = ",";
					}
				}
				subStmt.getToken(0).value = "Collection.newHashMap(";
				subStmt.getToken(subStmt.size() - 1).value = ")";

				// 添加依赖
				clazz.addImport(Collection.class.getName());

			} else if (token.isInvokeInit()) {// 在所有的构造函数前面都加个new
				Stmt subStmt = (Stmt) token.value;
				convertSubStmt(clazz, subStmt);
				// 追加一个关键字
				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new", null));

			} else if (token.isArrayInit()) {// 数组初始化,是没有子语句的
				Stmt subStmt = (Stmt) token.value;
				// 追加一个关键字
				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new", null));
			}

		}

		return stmt;
	}

}
