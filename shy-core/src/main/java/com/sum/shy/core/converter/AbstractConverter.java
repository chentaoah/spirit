package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public abstract class AbstractConverter implements Converter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<Line> lines,
			int index, Line line, Stmt stmt) {
		// 直接校验
		VariableTracker.track(clazz, method, block, line, stmt);
		// 将语句进行一定的转换
		sb.append(indent + convertStmt(clazz, stmt) + ";\n");

		return 0;
	}

	public static String convertStmt(Clazz clazz, Stmt stmt) {

		// 在所有的构造函数前面都加个new
		// 将所有的array和map都转换成方法调用
		for (int i = 0; i < stmt.tokens.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isSeparator() && ":".equals(token.value)) {
				token.value = ",";
			}
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
				subStmt.getToken(0).value = "Collection.newHashMap(";
				subStmt.getToken(subStmt.tokens.size() - 1).value = ")";

			} else if (token.isInvokeInit()) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(clazz, subStmt);
				// 如果是别名,则将类名替换一下
				String type = token.getMethodNameAtt();
				if (clazz.isAlias(type)) {
					subStmt.tokens.set(0, new Token(Constants.PREFIX_TOKEN, clazz.findImport(type), null));
				}
				// 追加一个关键字
				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new", null));

			} else if (token.isInvokeStatic()) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(clazz, subStmt);
				// 如果是别名,则将类名替换一下
				String type = token.getClassNameAtt();
				if (clazz.isAlias(type)) {
					String className = clazz.findImport(type);
					String value = (String) subStmt.getToken(0).value;
					value = className + value.substring(value.indexOf("."));
					subStmt.tokens.set(0, new Token(Constants.PREFIX_TOKEN, value, null));
				}

			} else if (token.isStaticVar()) {
				// 如果是别名,则将类名替换一下
				String type = token.getClassNameAtt();
				if (clazz.isAlias(type)) {
					String className = clazz.findImport(type);
					String value = (String) token.value;
					token.value = className + value.substring(value.indexOf("."));
				}

			} else if (token.isCast()) {
				// 如果是别名,则将类名替换一下
//				String type = token.getCastTypeAtt();
//				if (clazz.isAlias(type)) {
//					String className = clazz.findImport(type);
//					token.value = "(" + className + ")";
//				}

			}

		}

		return stmt.toString();
	}

}
