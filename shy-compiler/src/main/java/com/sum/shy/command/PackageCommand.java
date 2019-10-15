package com.sum.shy.command;

import java.util.List;

import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SMethod;

public class PackageCommand extends AbstractCommand {

	@Override
	public int handle(SClass clazz, SMethod method, String scope, List<String> lines, int index, String line) {
		// 如果是在根域下,则开始解析
		if ("static".equals(scope)) {
			clazz.packageStr = line.replace("package ", "").trim();
		}
		return 0;
	}

}