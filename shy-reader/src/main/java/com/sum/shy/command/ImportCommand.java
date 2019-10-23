package com.sum.shy.command;

import java.util.List;

import com.sum.shy.entity.Class;
import com.sum.shy.entity.Sentence;

public class ImportCommand extends AbstractCommand {

	@Override
	public int handle(String scope, Class clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			clazz.importStrs.add(sentence.getUnit(1));
		}
		return 0;
	}
}