package com.sum.shy.command;

import java.util.List;

import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public class IfCommand extends AbstractCommand {
	@Override
	public int handle(SClass clazz, SMethod method, String scope, List<String> lines, int index, String line) {

		if ("method".equals(scope)) {
			
		}
		return 0;
	}
}
