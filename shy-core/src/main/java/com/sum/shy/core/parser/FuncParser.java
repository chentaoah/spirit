package com.sum.shy.core.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.utils.LineUtils;

public class FuncParser implements Parser {

	@Override
	public int parse(Clazz clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 这里一定要trim一下
		List<String> list = Splitter.on(CharMatcher.anyOf("(,)")).trimResults().splitToList(stmt.get(1));
		// 方法名
		String name = list.get(0);
		// 开始遍历参数
		// TODO 这里参数还需要泛型
		List<Param> params = new ArrayList<>();
		for (int i = 1; i < list.size(); i++) {
			// 可能是user.say()无参数方法
			if (list.get(i).length() > 0) {
				String[] strs = list.get(i).split(" ");
				params.add(new Param(strs[0], strs[1]));
			}
		}

		// 添加方法
		Method method = new Method(Constants.NONE, name, params);
		// 这里简化了，不再尝试获取返回类型
		method.methodLines = LineUtils.getSubLines(lines, index);

		if (Constants.STATIC_SCOPE.equals(scope)) {
			clazz.staticMethods.add(method);
		} else if (Constants.CLASS_SCOPE.equals(scope)) {
			clazz.methods.add(method);
		}

		return method.methodLines.size() + 1;

	}

}