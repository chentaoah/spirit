package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

public class ForInConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {

		Token token = stmt.getToken(3);

		Type returnType = null;
		if (token.isVar()) {
			returnType = token.getTypeAtt();
		} else if (token.isInvoke()) {
			returnType = token.getReturnTypeAtt();
		}
		// TODO 这里还需要进一步的判断
		List<Type> genericTypes = returnType.getGenericTypes();
		Type genericType = genericTypes.get(0);

		String name = stmt.get(1);
		String express = stmt.get(3);

		String text = String.format("for (%s %s:%s){", genericType, name, express);

		method.addVariable(new Variable(block, genericType, name));
		// 直接返回拼接的字符串
		return new Stmt(text);
	}

}
