package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.parser.api.Parser;

public class ImportParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 类全名
		String className = stmt.get(1);
		// 类名
		String name = className.substring(className.lastIndexOf(".") + 1);
		// 别名
		String alias = stmt.size() == 3 ? stmt.get(2) : null;
		// 根据是否有别名分别添加到不同容器中
		if (alias == null) {
			clazz.importStrs.put(name, className);
		} else {
			clazz.importAliases.put(alias, className);
		}
		return 0;
	}

}