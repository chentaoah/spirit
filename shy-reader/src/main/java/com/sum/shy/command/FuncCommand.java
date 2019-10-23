package com.sum.shy.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.entity.Class;
import com.sum.shy.entity.Method;
import com.sum.shy.entity.Param;
import com.sum.shy.entity.Sentence;
import com.sum.shy.utils.LineUtils;

public class FuncCommand extends AbstractCommand {

	@Override
	public int handle(String scope, Class clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			return createMethod(clazz.staticMethods, lines, index, sentence);
		} else if ("class".equals(scope)) {
			return createMethod(clazz.methods, lines, index, sentence);
		}
		return 0;
	}

	private int createMethod(List<Method> methods, List<String> lines, int index, Sentence sentence) {

		String str = sentence.getReplacedStr(1);
		// 这里一定要trim一下
		List<String> list = Splitter.on(CharMatcher.anyOf("(,)")).trimResults().splitToList(str);
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
		// 创建方法
		Method method = new Method("var", name, params);
		methods.add(method);

		method.methodLines = LineUtils.getSubLines(lines, index);

		return method.methodLines.size() + 1;

	}
}