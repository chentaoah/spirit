package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;

public class ImportParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 类全名
		String fullName = stmt.get(1);
		// 类名
		String name = fullName.substring(fullName.lastIndexOf(".") + 1);
		// 别名
		String alias = stmt.size() == 3 ? stmt.get(2) : null;
		// 根据是否有别名分别添加到不同容器中
		if (alias == null) {
			clazz.importStrs.put(name, fullName);
		} else {
			clazz.importAliases.put(alias, fullName);
		}
		return 0;
	}

}