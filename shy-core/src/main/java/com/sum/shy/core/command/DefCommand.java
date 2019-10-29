package com.sum.shy.core.command;

import java.util.List;

import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Result;

public class DefCommand implements Command {

	@Override
	public Result analysis(List<String> lines, int index, String line, String syntax, List<String> units) {
		String type = units.get(1);
		String[] strs = units.get(2).split(",");
		for (String str : strs) {
			Context.get().clazz.defTypes.put(str, type);
		}
		return new Result(0, null);
	}

}
