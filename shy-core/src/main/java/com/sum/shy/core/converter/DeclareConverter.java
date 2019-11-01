package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Variable;

public class DeclareConverter extends AbstractConverter {
	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<String> lines,
			int index, String line, Stmt stmt) {
		// 追加一个参数声明到方法中
		method.addVariable(new Variable(block, stmt.get(1)));
		// 将语句进行一定的转换
		sb.append(indent + convertStmt(stmt) + " = null;\n");

		return 0;
	}
}