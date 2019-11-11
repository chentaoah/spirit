package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.analyzer.InvocationVisitor;
import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.NativeType;
import com.sum.shy.core.entity.Variable;

public class AssignmentConverter extends AbstractConverter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<Line> lines,
			int index, Line line, Stmt stmt) {
		// 直接校验
		VariableTracker.check(clazz, method, block, line, stmt);
		// 方法返回值推算
		InvocationVisitor.check(clazz, stmt);

		// 如果是单纯的变量,而不是成员变量,则需要进行类型声明
		Token token = stmt.getToken(0);
		if (token.isVar() && token.getNativeTypeAtt() == null) {
			// 如果没有,则在最前面追加类型
			NativeType nativeType = TypeDerivator.getNativeType(stmt);
			// 添加到头部类型引入(可以重复添加)
			clazz.addImport(nativeType);
			token.setNativeTypeAtt(nativeType);
			method.addVariable(new Variable(block, nativeType, (String) token.value));

			stmt.tokens.add(0, new Token(Constants.TYPE_TOKEN, nativeType.toString(), null));
		}

		// 将语句进行一定的转换
		sb.append(indent + convertStmt(clazz, stmt) + ";\n");
		return 0;

	}

}
