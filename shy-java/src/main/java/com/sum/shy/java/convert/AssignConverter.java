package com.sum.shy.java.convert;

import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.JavaConverter;

public class AssignConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {

		// 保留第一个var token
		Token token = stmt.getToken(0);

		if (line.text.contains("( s!=\"test\" ) && s==\"test\" && list.get(0) && s")) {
			System.out.println("");
		}
		JavaConverter.convertCommon(clazz, stmt);
		JavaConverter.convertEquals(clazz, stmt);
		JavaConverter.addLineEnd(clazz, stmt);

		if (token.isVar() && !token.isDeclaredAtt()) {
			stmt.tokens.add(0, new Token(Constants.TYPE_TOKEN, token.getTypeAtt(), null));
		}

		stmt.syntax = Constants.ASSIGN_SYNTAX;

		return stmt;

	}

}
