package com.sum.shy.java.convert;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.entity.Token;

public class ForInConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		Token var = stmt.getToken(1);
		String express = stmt.subStmt(3, stmt.size() - 1).toString();
		String text = String.format("for (%s %s : %s) {", var.getTypeAtt(), var.value, express);
		// 直接返回拼接的字符串
		return new Stmt(text);
	}

}
