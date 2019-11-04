package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;

public class ReturnConverter extends AbstractConverter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<Line> lines,
			int index, Line line, Stmt stmt) {
		// 直接校验
		VariableTracker.check(clazz, method, block, line, stmt);

		// 如果没有,则在最前面追加类型
		String type = TypeDerivator.getType(stmt);
		List<String> genericTypes = TypeDerivator.getGenericTypes(stmt);
		method.returnType = type;
		method.genericTypes = genericTypes;
		// 将语句进行一定的转换
		sb.append(indent + convertStmt(stmt) + ";\n");

		return 0;
	}
}
