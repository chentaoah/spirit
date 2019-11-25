package com.sum.shy.core.converter;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class ForConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		Token name = stmt.getToken(1);
		stmt.tokens.add(1, new Token(Constants.TYPE_TOKEN, name.getTypeAtt(), null));
		stmt.tokens.add(1, new Token(Constants.SEPARATOR_TOKEN, "(", null));
		stmt.tokens.add(stmt.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));
		// 直接返回拼接的字符串
		return stmt;

	}

}
