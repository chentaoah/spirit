package com.sum.shy.java.convert;

import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class NoneConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		return stmt;
	}

}
