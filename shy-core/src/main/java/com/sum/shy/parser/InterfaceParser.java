package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.core.doc.IClass;
import com.sum.shy.core.doc.Line;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Token;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.utils.LineUtils;

public class InterfaceParser implements Parser {

	@Override
	public int parse(IClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 设置类上面的注解
		clazz.annotations = Context.get().getAnnotations();
		clazz.category = Constants.INTERFACE_KEYWORD;
		clazz.typeName = stmt.get(1);
		try {
			if (Constants.EXTENDS_KEYWORD.equals(stmt.get(2))) {
				for (Token token : stmt.subStmt(3, stmt.size()).tokens) {
					if (token.isKeywordParam())
						clazz.interfaces.add(token.toString());
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
