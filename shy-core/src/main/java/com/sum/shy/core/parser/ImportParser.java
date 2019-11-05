package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class ImportParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 设置上下文中的
		clazz.importStrs.add(stmt.get(1));
		return 0;
	}

}