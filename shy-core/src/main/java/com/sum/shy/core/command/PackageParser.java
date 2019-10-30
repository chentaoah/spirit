package com.sum.shy.core.command;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Stmt;

public class PackageParser implements Parser {

	@Override
	public int parse(List<String> lines, int index, String line, Stmt stmt) {

		String packageStr = stmt.get(1);
		// 设置上下文中的
		Context.get().clazz.packageStr = packageStr;

		return 0;
	}

}