package com.sum.shy.core.converter;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class DeclareConverter extends DefaultConverter {
	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		// 将语句进行一定的转换
		stmt.tokens.add(new Token(Constants.SUFFIX_TOKEN, " = null;\n", null));
		return stmt;
	}
}
