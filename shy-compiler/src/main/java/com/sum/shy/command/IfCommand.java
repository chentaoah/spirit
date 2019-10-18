package com.sum.shy.command;

import java.util.List;

import com.sum.shy.core.Sentence;
import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public class IfCommand extends AbstractCommand {
	@Override
	public int handle(String scope, SClass clazz, SMethod method, List<String> lines, int index, Sentence sentence) {
		
		if ("method".equals(scope)) {

		}
		return 0;
	}
}
