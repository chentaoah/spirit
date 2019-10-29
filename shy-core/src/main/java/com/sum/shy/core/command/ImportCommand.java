package com.sum.shy.core.command;

import java.util.List;

import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Result;

public class ImportCommand implements Command {

	@Override
	public Result analysis(String line, String syntax, List<String> units)  {

		String importStr = units.get(1);
		// 设置上下文中的
		Context.get().clazz.importStrs.add(importStr);

		return new Result(0, null);
	}
}