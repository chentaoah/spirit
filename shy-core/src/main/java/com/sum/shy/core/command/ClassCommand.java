package com.sum.shy.core.command;

import java.util.Arrays;
import java.util.List;

import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Result;
import com.sum.shy.core.utils.LineUtils;

public class ClassCommand implements Command {

	@Override
	public Result analysis(String line, String syntax, List<String> words) {

		// 解析类名
		Clazz clazz = Context.get().clazz;

		clazz.className = words.get(1);
		try {
			if ("extends".equals(words.get(2))) {
				clazz.superName = words.get(3);
			}
			if ("impl".equals(words.get(4))) {
				clazz.interfaces = Arrays.asList(words.get(5).split(","));
			}

		} catch (Exception e) {
			// ignore
		}

		// 通过工具类来获取下面的所有行
		clazz.classLines = LineUtils.getSubLines(Context.get().lines, Context.get().lineNumber);

		return new Result(clazz.classLines.size() + 1, null);
	}

}
