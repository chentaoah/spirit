package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Stmt;

public class PackageParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<String> lines, int index, String line, Stmt stmt) {

		String packageStr = stmt.get(1);
		// 设置上下文中的
		clazz.packageStr = packageStr;

		return 0;
	}

}