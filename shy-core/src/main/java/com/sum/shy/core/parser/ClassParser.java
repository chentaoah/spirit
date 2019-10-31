package com.sum.shy.core.parser;

import java.util.Arrays;
import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.utils.LineUtils;

public class ClassParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<String> lines, int index, String line, Stmt stmt) {

		clazz.className = stmt.get(1);
		try {
			if ("extends".equals(stmt.get(2))) {
				clazz.superName = stmt.get(3);
			}
			if ("impl".equals(stmt.get(4))) {
				clazz.interfaces = Arrays.asList(stmt.get(5).split(","));
			}

		} catch (Exception e) {
			// ignore
		}

		// 通过工具类来获取下面的所有行
		clazz.classLines = LineUtils.getSubLines(lines, index);

		return clazz.classLines.size() + 1;
	}

}
