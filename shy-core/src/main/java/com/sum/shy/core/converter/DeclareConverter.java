package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Variable;

public class DeclareConverter extends AbstractConverter {
	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<Line> lines,
			int index, Line line, Stmt stmt) {

//		method.addVariable(new Variable(block, new CodeType(stmt.get(0)), stmt.get(1)));
		// 将语句进行一定的转换
		sb.append(indent + convertStmt(clazz, stmt) + " = null;\n");

		return 0;
	}
}
