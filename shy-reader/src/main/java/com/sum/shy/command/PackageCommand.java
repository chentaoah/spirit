package com.sum.shy.command;

import java.util.List;

import com.sum.shy.clazz.Clazz;
import com.sum.shy.sentence.Sentence;

public class PackageCommand extends AbstractCommand {

	@Override
	public int handle(String scope, Clazz clazz, List<String> lines, int index, Sentence sentence) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			clazz.packageStr = sentence.getUnit(1);
		}
		return 0;
	}

}