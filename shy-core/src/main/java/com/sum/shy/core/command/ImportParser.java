package com.sum.shy.core.command;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Stmt;

public class ImportParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<String> lines, int index, String line, Stmt stmt) {

		String importStr = stmt.get(1);
		// 设置上下文中的
		Context.get().clazz.importStrs.add(importStr);

		return 0;
	}

}