package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtField;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class FieldParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 变量名
		String name = stmt.get(0);
		// 这里不再直接推导类型
		clazz.addField(new CtField(scope, null, name, stmt, Context.get().getAnnotations()));

		return 0;
	}

}