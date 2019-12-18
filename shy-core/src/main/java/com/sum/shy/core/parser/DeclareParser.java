package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtField;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class DeclareParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 变量名
		String name = stmt.get(1);
		// 这里不再直接推导类型
		if (Constants.STATIC_SCOPE.equals(scope)) {
			clazz.addStaticField(new CtField(new CodeType(clazz, stmt.getToken(0)), name, new Stmt(name),
					Context.get().getAnnotations()));
		} else if (Constants.CLASS_SCOPE.equals(scope)) {
			clazz.addField(new CtField(new CodeType(clazz, stmt.getToken(0)), name, new Stmt(name),
					Context.get().getAnnotations()));
		}

		return 0;
	}

}