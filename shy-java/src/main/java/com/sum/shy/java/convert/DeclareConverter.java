package com.sum.shy.java.convert;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.java.JavaConverter;

public class DeclareConverter extends DefaultConverter {
	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {
		JavaConverter.addLineEnd(clazz, stmt);
		return stmt;
	}
}
