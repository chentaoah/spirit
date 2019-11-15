package com.sum.shy.core.parser;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.FastDerivator;
import com.sum.shy.core.analyzer.LexicalAnalyzer;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.api.Type;
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

		String desc = stmt.get(1);
		String methodName = desc.substring(0, desc.indexOf("("));// 名称
		List<Param> params = new ArrayList<>();// 参数
		List<String> list = LexicalAnalyzer.getWords(desc.substring(desc.indexOf("(") + 1, desc.indexOf(")")));
		for (int i = 0; i < list.size(); i = i + 3) {
			String type = list.get(i);
			String name = list.get(i + 1);
			// 根据字符串字面意思,获取类型
			params.add(new Param(null/* new CodeType(type) */, name));
		}

		// 添加方法
		Method method = new Method(null, methodName, params);
		method.methodLines = LineUtils.getSubLines(lines, index);
		// 快速推导
		Type returnType = FastDerivator.getReturnType(clazz, method);
		method.returnType = returnType;

		if (Constants.STATIC_SCOPE.equals(scope)) {
			clazz.staticMethods.add(method);
		} else if (Constants.CLASS_SCOPE.equals(scope)) {
			clazz.methods.add(method);
		}

		return method.methodLines.size() + 1;

	}

}