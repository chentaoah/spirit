package com.sum.shy.core.converter;

import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class JudgeInvokeConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		String var = stmt.get(0);
		String invoke = stmt.get(2);
		String text = String.format("if (%s != null)\n%s%s%s;", var, indent + "\t", var, invoke);
		return new Stmt(text);
	}

}
