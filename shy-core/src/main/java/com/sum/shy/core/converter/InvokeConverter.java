package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.api.Converter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class InvokeConverter implements Converter {

	@Override
	public int convert(StringBuilder sb, String indent, Clazz clazz, Method method, List<String> lines, int index,
			String line, Stmt stmt) {

		// 将语句进行一定的转换
		sb.append("\t\t" + convertStmt(stmt) + ";\n");

		return 0;
	}

	private String convertStmt(Stmt stmt) {

		// 在所有的构造函数前面都加个new
		// 将所有的array和map都转换成方法调用
		for (Token token : stmt.tokens) {
			if ("separator".equals(token.type) && ":".equals(token.value)) {
				token.value = ",";
			}
			if ("array".equals(token.type)) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(subStmt);
				subStmt.tokens.get(0).value = "Collection.newArrayList(";
				subStmt.tokens.get(subStmt.tokens.size() - 1).value = ")";

			} else if ("map".equals(token.type)) {
				Stmt subStmt = (Stmt) token.value;
				// 先将子语句转换
				convertStmt(subStmt);
				subStmt.tokens.get(0).value = "Collection.newHashMap(";
				subStmt.tokens.get(subStmt.tokens.size() - 1).value = ")";

			} else if ("invoke_init".equals(token.type)) {
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
