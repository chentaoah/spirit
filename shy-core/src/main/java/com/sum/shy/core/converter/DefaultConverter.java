package com.sum.shy.core.converter;

import com.sum.shy.core.api.Converter;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class DefaultConverter implements Converter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		return convertStmt(clazz, stmt);
	}

	public static Stmt convertStmt(CtClass clazz, Stmt stmt) {
		// 将语句进行一定的转换
		stmt = convertSubStmt(clazz, stmt);
		// 添加一个后缀
		stmt.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ";", null));

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

			} else if (token.isMap()) {
				Stmt subStmt = (Stmt) token.value;
				convertSubStmt(clazz, subStmt);
//				if (token.isSeparator() && ":".equals(token.value)) {
//					token.value = ",";
//				}
				subStmt.getToken(0).value = "Collection.newHashMap(";
				subStmt.getToken(subStmt.size() - 1).value = ")";

			} else if (token.isInvokeInit()) {// 在所有的构造函数前面都加个new
				Stmt subStmt = (Stmt) token.value;
				convertSubStmt(clazz, subStmt);
				// 追加一个关键字
				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new", null));

			}

		}

		return stmt;
	}

}
