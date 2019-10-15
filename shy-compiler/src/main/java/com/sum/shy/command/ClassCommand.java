package com.sum.shy.command;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Splitter;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public class ClassCommand extends AbstractCommand {
	@Override
	public int handle(SClass clazz, SMethod method, String scope, List<String> lines, int index, String line) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			// 拆分字符串
			List<String> strs = Splitter.on("||").trimResults().splitToList(
					line.replace("class ", "").replace(" extends ", "||").replace(" impl ", "||").replace("{", ""));
			// 解析类名
			clazz.className = strs.get(0);
			// 解析父类和接口
			if (line.contains(" extends ")) {
				clazz.superClass = strs.get(1);
			}
			if (line.contains(" impl ")) {
				clazz.interfaces.addAll(Arrays.asList(strs.get(strs.size() - 1).split(",")));
			}
			// 找到子域的结束符"}"
			for (int i = index + 1, count = 1; i < lines.size(); i++) {
				String str = lines.get(i);
				if (str.contains("{")) {
					count++;
				} else if (str.contains("}")) {
					count--;
				}
				if (count == 0) {
					break;
				}
				clazz.classLines.add(str);
			}
			return clazz.classLines.size() + 1;
		}
		return 0;
	}
}
