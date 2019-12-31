package com.sum.shy.java.convert;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;

public class DefaultConverter implements Converter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		return convertStmt(clazz, stmt);
	}

	public static Stmt convertStmt(CtClass clazz, Stmt stmt) {
		JavaConverter.convertCommon(clazz, stmt);
		JavaConverter.addLineEnd(clazz, stmt);
		return stmt;

	}

}
