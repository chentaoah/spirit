package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.NativeType;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Variable;
import com.sum.shy.core.utils.ReflectUtils;

public class DeclareConverter extends AbstractConverter {
	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<Line> lines,
			int index, Line line, Stmt stmt) {
		// 追加一个参数声明到方法中
		// TODO 这里还需要考虑泛型是如何声明的
		NativeType nativeType = ReflectUtils.getNativeType(clazz, stmt.get(0));
		method.addVariable(new Variable(block, nativeType, stmt.get(1)));
		// 将语句进行一定的转换
		sb.append(indent + convertStmt(stmt) + " = null;\n");

		return 0;
	}
}
