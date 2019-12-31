package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtField;
import com.sum.shy.entity.Context;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.type.CodeType;

public class DeclareParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 变量名
		String name = stmt.get(1);
		// 这里不再直接推导类型
		clazz.addField(new CtField(Context.get().getAnnotations(), scope, new CodeType(clazz, stmt.getToken(0)), name,
				new Stmt(name)));

		return 0;
	}

}