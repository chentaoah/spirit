package com.sum.shy.java.convert;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.entity.Constants;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.entity.Token;
import com.sum.shy.java.JavaConverter;

public class ForConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		Token var = stmt.getToken(1);
		stmt.tokens.add(1, new Token(Constants.TYPE_TOKEN, var.getTypeAtt(), null));
		JavaConverter.insertBrackets(clazz, stmt);
		return stmt;

	}

}
