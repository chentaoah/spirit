package com.sum.shy.java.convert;

import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.JavaConverter;
import com.sum.shy.java.api.Converter;

public class DefaultConverter implements Converter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		return convertStmt(clazz, stmt);
	}

	public static Stmt convertStmt(CtClass clazz, Stmt stmt) {
		// 将语句进行一定的转换
		JavaConverter.convertCommon(clazz, stmt);
		// 这个添加的后缀,使得后面不会加上空格
		stmt.tokens.add(new Token(Constants.SUFFIX_TOKEN, ";", null));

		return stmt;

	}

}
