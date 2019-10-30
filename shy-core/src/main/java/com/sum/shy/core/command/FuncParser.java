package com.sum.shy.core.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.utils.LineUtils;

public class FuncParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<String> lines, int index, String line, Stmt stmt) {

		// 这里一定要trim一下
		List<String> list = Splitter.on(CharMatcher.anyOf("(,)")).trimResults().splitToList(stmt.get(1));
		// 方法名
		String name = list.get(0);
		// 开始遍历参数
		List<Param> params = new ArrayList<>();
		for (int i = 1; i < list.size(); i++) {
			// 可能是user.say()无参数方法
			if (list.get(i).length() > 0) {
				String[] strs = list.get(i).split(" ");
				params.add(new Param(strs[0], strs[1]));
			}
		}

		// 获取子行
		List<String> subLines = LineUtils.getSubLines(lines, index);
		// 寻找return
		String returnType = "unknown";
		// 如果是集合类型,还要获取泛型
		List<String> genericTypes = null;
		for (String subLine : subLines) {
			if (subLine.trim().startsWith("return ")) {
				Stmt subStmt = Stmt.create(line);
				returnType = TypeDerivator.getType(subStmt);
				genericTypes = TypeDerivator.getGenericTypes(subStmt);
			}
		}

		// 添加方法
		Method method = new Method(returnType, genericTypes, name, params);
		method.methodLines = subLines;
		if ("static".equals(scope)) {
			clazz.staticMethods.add(method);
		} else if ("class".equals(scope)) {
			clazz.methods.add(method);
		}

		return method.methodLines.size() + 1;

	}

}