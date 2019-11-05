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
		VariableTracker.check(clazz, method, block, line, stmt);
		// 将语句进行一定的转换
		sb.append(indent + convertStmt(stmt) + ";\n");

		return 0;
	}

	public static String convertType(String type, List<String> genericTypes) {
		if (Constants.STR_TYPE.equals(type)) {
			return "String";
		} else if (Constants.ARRAY_TYPE.equals(type)) {
			return "List<" + convertGenericType(genericTypes.get(0)) + ">";
		} else if (Constants.MAP_TYPE.equals(type)) {
			return "Map<" + convertGenericType(genericTypes.get(0)) + "," + convertGenericType(genericTypes.get(1))
					+ ">";
		} else if (Constants.OBJ_TYPE.equals(type)) {
			return "Object";
		} else if (Constants.NONE.equals(type)) {
			return "void";
		}
		return type;
	}

	public static String convertGenericType(String type) {
		if (Constants.BOOLEAN_TYPE.equals(type)) {
			return "Boolean";
		} else if (Constants.INT_TYPE.equals(type)) {
			return "Integer";
		} else if (Constants.DOUBLE_TYPE.equals(type)) {
			return "Double";
		} else if (Constants.STR_TYPE.equals(type)) {
			return "String";
		} else if (Constants.OBJ_TYPE.equals(type)) {
			return "Object";
		}
		return type;

	}

	public static String convertStmt(Stmt stmt) {

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
				convertStmt(subStmt);
				subStmt.getToken(0).value = "Collection.newArrayList(";
				subStmt.getToken(subStmt.tokens.size() - 1).value = ")";

			} else if (token.isMap()) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(subStmt);
				subStmt.getToken(0).value = "Collection.newHashMap(";
				subStmt.getToken(subStmt.tokens.size() - 1).value = ")";

			} else if (token.isInvokeInit()) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(subStmt);
				// 追加一个关键字
				subStmt.tokens.add(0, new Token("keyword", "new", null));
			}
		}

		return stmt.toString();
	}

}
