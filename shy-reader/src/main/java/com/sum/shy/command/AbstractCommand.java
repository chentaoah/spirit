package com.sum.shy.command;

import java.util.List;

import com.sum.shy.api.Command;
import com.sum.shy.clazz.Clazz;
import com.sum.shy.sentence.Sentence;

public abstract class AbstractCommand implements Command {

	@Override
	public int handle(String scope, Clazz clazz, List<String> lines, int index, Sentence sentence) {
		// 最后返回跳跃数
		return 0;
	}

}
