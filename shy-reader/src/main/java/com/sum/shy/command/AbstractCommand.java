package com.sum.shy.command;

import java.util.List;

import com.sum.shy.api.Command;
import com.sum.shy.entity.Class;
import com.sum.shy.entity.Sentence;

public abstract class AbstractCommand implements Command {

	@Override
	public int handle(String scope, Class clazz, List<String> lines, int index, Sentence sentence) {
		// 最后返回跳跃数
		return 0;
	}

}
