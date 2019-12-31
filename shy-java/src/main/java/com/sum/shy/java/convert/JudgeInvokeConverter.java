package com.sum.shy.java.convert;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;

public class JudgeInvokeConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		String var = stmt.get(0);
		String invoke = stmt.get(2);
		String text = String.format("if (%s != null)\n%s%s%s;", var, indent + "\t", var, invoke);
		return new Stmt(text);
	}

}
