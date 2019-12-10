package com.sum.shy.core.parser;

import java.util.List;

import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.LineUtils;

public class ClassParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 设置类上面的注解
		clazz.annotations = Context.get().getAnnotations();
		// 类别 interface abstract class
		clazz.category = stmt.get(0);

		// 这里这两个关键字的位置,可能并不是固定的,这就尴尬了
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isKeyword()) {
				if ("class".equals(token.value)) {
					clazz.typeName = stmt.get(i + 1);// 类名

				} else if ("extends".equals(token.value)) {
					clazz.superName = stmt.get(i + 1);// 父类名称

				} else if ("impl".equals(token.value)) {
					for (int j = i + 1; j < stmt.size(); j++) {
						Token nextToken = stmt.getToken(j);
						if (nextToken.isKeywordParam()) {
							clazz.interfaces.add(nextToken.value.toString());
						} else if (nextToken.isKeyword()) {
							break;
						}
					}
				}
			}
		}

		// 通过工具类来获取下面的所有行
		clazz.classLines = LineUtils.getSubLines(lines, index);

		return clazz.classLines.size() + 1;
	}

}
