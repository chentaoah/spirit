package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Stmt;

public class DefParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<String> lines, int index, String line, Stmt stmt) {

		String type = stmt.get(1);
		String[] strs = stmt.get(2).split(",");
		for (String str : strs) {
			clazz.defTypes.put(str, type);
		}
		return 0;

	}

}
