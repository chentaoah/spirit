package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class ImportParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 类全名
		String fullName = stmt.get(1);
		// 类名
		String name = fullName.substring(fullName.lastIndexOf(".") + 1);
		// 别名
		String alias = stmt.size() == 3 ? stmt.get(2) : null;
		clazz.importStrs.put(alias == null ? alias : name, fullName);
		return 0;
	}

}