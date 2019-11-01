package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.VariableChecker;
import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

public class AssignmentConverter extends AbstractConverter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<String> lines,
			int index, String line, Stmt stmt) {

		// 如果是单纯的变量,而不是成员变量,则需要进行类型声明
		String tokenType = stmt.getToken(0).type;
		String name = (String) stmt.getToken(0).value;

		if ("var".equals(tokenType)) {
			if (!VariableChecker.seachInClass(clazz, method, block, name)) {
				// 追加变量
				method.addVariable(new Variable(block, name));
				// 直接校验
				VariableChecker.check(clazz, method, block, stmt);
				// 如果没有,则在最前面追加类型
				String type = TypeDerivator.getType(stmt);
				List<String> genericTypes = TypeDerivator.getGenericTypes(stmt);
				String str = convertType(type, genericTypes);
				stmt.tokens.add(0, new Token("type", str, null));

			}
		} else {
			// 直接校验
			VariableChecker.check(clazz, method, block, stmt);
		}

		return super.convert(sb, block, indent, clazz, method, lines, index, line, stmt);

	}

}
