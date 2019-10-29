package com.sum.shy.core.command;

import java.util.List;

import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Result;

public class PackageCommand implements Command {

	@Override
	public Result analysis(String line, String syntax, List<String> units) {

		String packageStr = units.get(1);
		// 设置上下文中的
		Context.get().clazz.packageStr = packageStr;

		return new Result(0, null);
	}

}