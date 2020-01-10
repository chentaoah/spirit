package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.clazz.CtField;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.parser.api.Parser;

public class FieldParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 变量名
		String name = stmt.get(0);
		// 这里不再直接推导类型
		clazz.addField(new CtField(Context.get().getAnnotations(), scope, null, name, stmt));

		return 0;
	}

}