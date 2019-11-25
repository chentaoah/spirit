package com.sum.shy.core.converter;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

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

		Type type = null;
		if (returnType.isList()) {
			type = returnType.getGenericTypes().get(0);
		} else if (returnType.isMap()) {
			type = returnType.getGenericTypes().get(0);
		}

		String name = stmt.get(1);
		String collection = stmt.get(3);

		String text = String.format("for (%s %s:%s){", type, name, collection);
		// 直接返回拼接的字符串
		return new Stmt(text);
	}

}
