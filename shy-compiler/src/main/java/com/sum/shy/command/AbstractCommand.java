package com.sum.shy.command;

import java.util.List;

import com.sum.shy.api.Command;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public abstract class AbstractCommand implements Command {

	@Override
	public int handle(SClass clazz, SMethod method, String scope, List<String> lines, int index, String line) {
		// 最后返回跳跃数
		return 0;
	}

}
