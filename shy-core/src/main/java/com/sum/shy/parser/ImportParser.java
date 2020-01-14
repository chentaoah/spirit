package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.utils.TypeUtils;

public class ImportParser implements Parser {

	@Override
	public int parse(IClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {
		// 类全名
		String className = stmt.get(1);
		// 根据是否有别名分别添加到不同容器中
		if (stmt.size() == 2) {
			String name = TypeUtils.getTypeNameByClass(className);
			clazz.importStrs.put(name, className);

		} else if (stmt.size() == 3) {
			String alias = stmt.get(2);
			clazz.importAliases.put(alias, className);
		}
		return 0;
	}

}