package com.sum.shy.command;

import java.util.Arrays;
import java.util.List;

import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public class ClassCommand extends AbstractCommand {

	@Override
	public int handle(String scope, SClass clazz, SMethod method, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {

			// 解析类名
			clazz.className = sentence.units.get(1).str;
			if ("extends".equals(sentence.units.get(2).str)) {
				clazz.superClass = sentence.units.get(3).str;
			}
			if ("impl".equals(sentence.units.get(4).str)) {
				clazz.interfaces = Arrays.asList(sentence.units.get(5).str.split(","));
			}

			// 找到子域的结束符"}"
			for (int i = index + 1, count = 1; i < lines.size(); i++) {
				String line = lines.get(i);
				for (int j = 0; j < line.length(); j++) {
					if (line.charAt(j) == '{') {
						count++;
					} else if (line.charAt(j) == '}') {
						count--;
					}
				}
				if (count == 0) {
					break;
				}
				clazz.classLines.add(line);
			}
			return clazz.classLines.size() + 1;
		}
		return 0;
	}
}
