package com.sum.shy.command;

import java.util.List;

import com.sum.shy.entity.Class;
import com.sum.shy.entity.Sentence;

public class DefCommand extends AbstractCommand {

	@Override
	public int handle(String scope, Class clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			String type = sentence.getUnit(1);
			String[] strs = sentence.getUnit(2).split(",");
			for (String str : strs) {
				clazz.defTypes.put(str, type);
			}
		}
		return 0;
	}

}
