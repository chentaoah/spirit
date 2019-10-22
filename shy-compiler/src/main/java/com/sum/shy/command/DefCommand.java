package com.sum.shy.command;

import java.util.List;

import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;

public class DefCommand extends AbstractCommand {

	@Override
	public int handle(String scope, SClass clazz, List<String> lines, int index, Sentence sentence) {
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
