package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.entity.Context;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.parser.api.Parser;

public class AnnotationParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 添加到注解到上下文
		Context.get().annotations.add(stmt.toString());
		return 0;
	}

}
