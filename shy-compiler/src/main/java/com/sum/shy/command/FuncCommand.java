package com.sum.shy.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;
import com.sum.shy.entity.SVar;

public class FuncCommand extends AbstractCommand {
	@Override
	public int handle(SClass clazz, SMethod method, String scope, List<String> lines, int index, String line) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			return createMethod(clazz.staticMethods, lines, index, line);
		} else if ("class".equals(scope)) {
			return createMethod(clazz.methods, lines, index, line);
		}
		return 0;
	}

	private int createMethod(List<SMethod> methods, List<String> lines, int index, String line) {
		// 剔除关键字
		String str = line.replace("func ", "").replace("{", "");
		// 名称
		String name = str.substring(0, str.indexOf("("));
		// 参数
		List<String> paramStrs = Splitter.on(",").trimResults()
				.splitToList(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
		List<SVar> vars = new ArrayList<>();
		for (String paramStr : paramStrs) {
			List<String> list = Splitter.on(" ").trimResults().splitToList(paramStr);
			vars.add(new SVar(list.get(0), list.get(1), ""));
		}
		// 创建方法
		SMethod method = new SMethod("", name, vars);
		// 找到子域的结束符"}"
		for (int i = index + 1, count = 1; i < lines.size(); i++) {
			String text = lines.get(i);
			if (text.contains("{")) {
				count++;
			} else if (text.contains("}")) {
				count--;
			}
			if (count == 0) {
				break;
			}
			method.methodLines.add(text);
		}
		methods.add(method);
		return method.methodLines.size() + 1;

	}
}