package com.sum.shy.command;

import java.util.Arrays;
import java.util.List;

import com.sum.shy.clazz.Clazz;
import com.sum.shy.sentence.Sentence;
import com.sum.shy.utils.LineUtils;

public class ClassCommand extends AbstractCommand {

	@Override
	public int handle(String scope, Clazz clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {

			// 解析类名
			clazz.className = sentence.getStr(1);

			if ("extends".equals(sentence.getStr(2))) {
				clazz.superName = sentence.getStr(3);
			}

			if ("impl".equals(sentence.getStr(4))) {
				clazz.interfaces = Arrays.asList(sentence.getStr(5).split(","));
			}

			// 通过工具类来获取下面的所有行
			clazz.classLines = LineUtils.getSubLines(lines, index);

		}
		return clazz.classLines.size() + 1;
	}
}