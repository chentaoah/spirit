package com.sum.shy.parser;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.CoopClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.utils.LineUtils;

public class ClassParser implements Parser {

	@Override
	public int parse(IClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 第一个关键字
		String keyword = stmt.get(0);
		if (Constants.ABSTRACT_KEYWORD.equals(keyword))
			stmt = stmt.subStmt(1, stmt.size());// 截取一下
		// 类名
		String typeName = stmt.get(1);
		if (!clazz.typeName.equals(typeName)) // 判断是不是内部类,判断依据是类名和文件名是否一致
			clazz = new CoopClass(clazz, typeName);// 新建一个内部类

		clazz.setAnnotations(Context.get().getAnnotations());
		clazz.category = keyword;
		clazz.typeName = typeName;

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isKeyword()) {
				if (Constants.EXTENDS_KEYWORD.equals(token.toString())) {
					clazz.superName = stmt.get(i + 1);// 父类名称

				} else if (Constants.IMPL_KEYWORD.equals(token.toString())) {
					for (int j = i + 1; j < stmt.size(); j++) {
						Token nextToken = stmt.getToken(j);
						if (nextToken.isKeywordParam()) {
							clazz.interfaces.add(nextToken.toString());
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
