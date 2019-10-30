package com.sum.shy.core.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.analyzer.LexicalAnalyzer;
import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Result;
import com.sum.shy.core.utils.LineUtils;

public class FuncCommand implements Command {

	@Override
	public Result analysis(String line, String syntax, List<String> words) {

		// 这里一定要trim一下
		List<String> list = Splitter.on(CharMatcher.anyOf("(,)")).trimResults().splitToList(words.get(1));
		// 方法名
		String name = list.get(0);
		// 开始遍历参数
		List<Param> params = new ArrayList<>();
		for (int i = 1; i < list.size(); i++) {
			// 可能是user.say()无参数方法
			if (list.get(i).length() > 0) {
				String[] strs = list.get(i).split(" ");
				params.add(new Param(strs[0], strs[1]));
			}
		}

		// 获取子行
		List<String> subLines = LineUtils.getSubLines(Context.get().lines, Context.get().lineNumber);
		// 寻找return
		String returnType = "unknown";
		for (String subLine : subLines) {
			if (subLine.startsWith("return ")) {
				List<String> subWords = LexicalAnalyzer.analysis(subLine);
				returnType = SemanticDelegate.getTypeByWords(subWords);
			}
		}

		// 添加方法
		Method method = new Method(returnType, name, params);
		method.methodLines = subLines;
		Context context = Context.get();
		if ("static".equals(context.scope)) {
			context.clazz.staticMethods.add(method);
		} else if ("class".equals(context.scope)) {
			context.clazz.methods.add(method);
		}

		return new Result(method.methodLines.size() + 1, null);

	}

}