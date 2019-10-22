package com.sum.shy.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;
import com.sum.shy.entity.SParam;
import com.sum.shy.utils.LineUtils;

public class FuncCommand extends AbstractCommand {

	@Override
	public int handle(String scope, SClass clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			return createMethod(clazz.staticMethods, lines, index, sentence);
		} else if ("class".equals(scope)) {
			return createMethod(clazz.methods, lines, index, sentence);
		}
		return 0;
	}

	private int createMethod(List<SMethod> methods, List<String> lines, int index, Sentence sentence) {

		String str = sentence.getUnitStr(1);
		// 这里一定要trim一下
		List<String> list = Splitter.on(CharMatcher.anyOf("(,)")).trimResults().splitToList(str);
		// 方法名
		String name = list.get(0);
		// 开始遍历参数
		List<SParam> params = new ArrayList<>();
		for (int i = 1; i < list.size(); i++) {
			// 可能是user.say()无参数方法
			if (list.get(i).length() > 0) {
				String[] strs = list.get(i).split(" ");
				params.add(new SParam(strs[0], strs[1]));
			}
		}
		// 创建方法
		SMethod method = new SMethod("var", name, params);
		methods.add(method);

		method.methodLines = LineUtils.getSubLines(lines, index);

		return method.methodLines.size() + 1;

	}
}