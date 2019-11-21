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
		// 将语句进行一定的转换
		stmt = convertStmt(clazz, stmt);
		return stmt;
	}

	public static Stmt convertStmt(CtClass clazz, Stmt stmt) {

		// 在所有的构造函数前面都加个new
		// 将所有的array和map都转换成方法调用
		for (int i = 0; i < stmt.tokens.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isArray()) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(clazz, subStmt);
				subStmt.getToken(0).value = "Collection.newArrayList(";
				subStmt.getToken(subStmt.tokens.size() - 1).value = ")";

			} else if (token.isMap()) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(clazz, subStmt);
//				if (token.isSeparator() && ":".equals(token.value)) {
//					token.value = ",";
//				}
				subStmt.getToken(0).value = "Collection.newHashMap(";
				subStmt.getToken(subStmt.tokens.size() - 1).value = ")";

			} else if (token.isInvokeInit()) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(clazz, subStmt);
				// 追加一个关键字
				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new", null));

			}

		}
		// 添加一个后缀
		stmt.tokens.add(new Token(Constants.SUFFIX_TOKEN, ";\n", null));
		return stmt;
	}

}
