package com.sum.shy.core.command;

import java.util.List;

import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Result;

public class DefCommand implements Command {

	@Override
	public Result analysis(String line, String syntax, List<String> words) {
		String type = words.get(1);
		String[] strs = words.get(2).split(",");
		for (String str : strs) {
			Context.get().clazz.defTypes.put(str, type);
		}
		return new Result(0, null);
	}

}
