package com.sum.shy.java.convert;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class JudgeInvokeConverter extends DefaultConverter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {
		String var = stmt.get(0);
		String invoke = stmt.get(2);
		String text = String.format("if (%s != null)\n%s%s%s;", var, indent + "\t", var, invoke);
		return new Stmt(text);
	}

}
