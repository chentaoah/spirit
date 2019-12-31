package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.entity.Context;
import com.sum.shy.entity.Line;
import com.sum.shy.entity.Stmt;
import com.sum.shy.entity.Token;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.utils.LineUtils;

public class InterfaceParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 设置类上面的注解
		clazz.annotations = Context.get().getAnnotations();
		clazz.category = "interface";
		clazz.typeName = stmt.get(1);
		try {
			if ("extends".equals(stmt.get(2))) {
				for (Token token : stmt.tokens.subList(3, stmt.size())) {
					if (token.isKeywordParam())
						clazz.interfaces.add(token.value.toString());
				}
			}

		} catch (Exception e) {
			// ignore
		}

		// 通过工具类来获取下面的所有行
		clazz.classLines = LineUtils.getSubLines(lines, index);

		return clazz.classLines.size() + 1;
	}

}