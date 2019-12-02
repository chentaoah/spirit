package com.sum.shy.core.parser;

import java.util.Arrays;
import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.utils.LineUtils;

public class ClassParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 设置类上面的注解
		clazz.annotations = Context.get().getAnnotations();

		clazz.typeName = stmt.get(1);
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
