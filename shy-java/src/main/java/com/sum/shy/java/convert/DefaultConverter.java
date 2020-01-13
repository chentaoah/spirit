package com.sum.shy.java.convert;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;

public class DefaultConverter implements Converter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {
		return convertStmt(clazz, stmt);
	}

	public static Stmt convertStmt(IClass clazz, Stmt stmt) {
		JavaConverter.convertCommon(clazz, stmt);
		JavaConverter.addLineEnd(clazz, stmt);
		return stmt;

	}

}
