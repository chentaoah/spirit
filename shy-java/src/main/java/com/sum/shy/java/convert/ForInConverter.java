package com.sum.shy.java.convert;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class ForInConverter extends DefaultConverter {

	@Override
	public Stmt convert(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {
		Token var = stmt.getToken(1);
		String express = stmt.subStmt(3, stmt.size() - 1).toString();
		String text = String.format("for (%s %s : %s) {", var.getTypeAtt(), var.value, express);
		// 直接返回拼接的字符串
		return new Stmt(text);
	}

}
