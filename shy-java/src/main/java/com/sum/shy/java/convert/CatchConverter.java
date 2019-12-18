package com.sum.shy.java.convert;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class CatchConverter extends DeclareConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		stmt.tokens.add(2, new Token(Constants.SEPARATOR_TOKEN, "(", null));
		stmt.tokens.add(5, new Token(Constants.SEPARATOR_TOKEN, ")", null));
		return stmt;
	}

}
