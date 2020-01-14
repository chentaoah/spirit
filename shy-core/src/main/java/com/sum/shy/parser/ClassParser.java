package com.sum.shy.parser;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.CoopClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.utils.LineUtils;

public class ClassParser implements Parser {

	@Override
	public int parse(IClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 这里这两个关键字的位置,可能并不是固定的,这就尴尬了
		String typeName = null;
		String superName = null;
		List<String> interfaces = new ArrayList<>();

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			// 如果是关键字
			if (token.isKeyword()) {
				if ("class".equals(token.value)) {
					typeName = stmt.get(i + 1);// 类名

				} else if ("extends".equals(token.value)) {
					superName = stmt.get(i + 1);// 父类名称

				} else if ("impl".equals(token.value)) {
					for (int j = i + 1; j < stmt.size(); j++) {
						Token nextToken = stmt.getToken(j);
						if (nextToken.isKeywordParam()) {
							interfaces.add(nextToken.value.toString());
						} else if (nextToken.isKeyword()) {
							break;
						}
					}
				}
			}
		}

		// 判断是不是内部类,判断依据是类名和文件名是否一致
		if (!clazz.typeName.equals(typeName)) {
			// 新建一个内部类
			CoopClass innerClass = new CoopClass();
			// 内部类指向了主类
			innerClass.mainClass = clazz;
			// 主类包含了内部类
			clazz.coopClasses.put(typeName, innerClass);

			clazz = innerClass;
		}

		// 设置类上面的注解
		clazz.setAnnotations(Context.get().getAnnotations());
		// 类别 interface abstract class
		clazz.category = stmt.get(0);
		// 类名
		clazz.typeName = typeName;
		// 父类名
		clazz.superName = superName;
		// 接口
		clazz.interfaces = interfaces;

		// 通过工具类来获取下面的所有行
		clazz.classLines = LineUtils.getSubLines(lines, index);

		return clazz.classLines.size() + 1;
	}

}
