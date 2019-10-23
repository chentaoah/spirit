package com.sum.shy.command;

import java.util.Arrays;
import java.util.List;

import com.sum.shy.entity.Class;
import com.sum.shy.entity.Sentence;
import com.sum.shy.utils.LineUtils;

public class ClassCommand extends AbstractCommand {

	@Override
	public int handle(String scope, Class clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {

			// 解析类名
			clazz.className = sentence.getUnit(1);

			if ("extends".equals(sentence.getUnit(2))) {
				clazz.superClass = sentence.getUnit(3);
			}

			if ("impl".equals(sentence.getUnit(4))) {
				clazz.interfaces = Arrays.asList(sentence.getUnit(5).split(","));
			}

			// 通过工具类来获取下面的所有行
			clazz.classLines = LineUtils.getSubLines(lines, index);

		}
		return clazz.classLines.size() + 1;
	}
}
