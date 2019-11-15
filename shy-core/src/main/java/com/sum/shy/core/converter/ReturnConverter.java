package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.InvocationVisitor;
import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;

public class ReturnConverter extends AbstractConverter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<Line> lines,
			int index, Line line, Stmt stmt) {
		// 直接校验
//		VariableTracker.check(clazz, method, block, line, stmt);
//		// 方法返回值推算
//		InvocationVisitor.check(clazz, stmt);
//		// 如果没有,则在最前面追加类型
//		Type nativeType = TypeDerivator.getType(stmt);
//		// 添加到头部类型引入(可以重复添加)
//		clazz.addImport(nativeType);
//		// 这个时候给方法追加返回类型
//		if (method.returnType.isVoid() && nativeType != null) {
//			method.returnType = nativeType;
//		}
//		// 将语句进行一定的转换
//		sb.append(indent + convertStmt(clazz, stmt) + ";\n");

		return 0;
	}
}
