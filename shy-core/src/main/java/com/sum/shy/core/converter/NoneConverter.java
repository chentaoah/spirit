package com.sum.shy.core.converter;

import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class NoneConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		return stmt;
	}

}
