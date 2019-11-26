package com.sum.shy.core.converter;

import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class ForInConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		Token var = stmt.getToken(1);
		Token express = stmt.getToken(3);
		String text = String.format("for (%s %s : %s) {", var.getTypeAtt(), var.value, express.value);
		// 直接返回拼接的字符串
		return new Stmt(text);
	}

}
