package com.sum.shy.command;

import java.util.List;

import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public class ImportCommand extends AbstractCommand {

	@Override
	public int handle(String scope, SClass clazz, SMethod method, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			clazz.importStrs.add(sentence.units.get(1));
		}
		return 0;
	}
}