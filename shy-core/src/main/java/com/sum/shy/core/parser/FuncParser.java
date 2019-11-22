package com.sum.shy.core.parser;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.LexicalAnalyzer;
import com.sum.shy.core.api.Parser;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.utils.LineUtils;

public class FuncParser implements Parser {

	@Override
	public int parse(CtClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		String methodDesc = stmt.get(1);
		String methodName = methodDesc.substring(0, methodDesc.indexOf("("));// 名称
		List<Param> params = new ArrayList<>();// 参数
		List<String> list = LexicalAnalyzer
				.getWords(methodDesc.substring(methodDesc.indexOf("(") + 1, methodDesc.indexOf(")")));
		for (int i = 0; i < list.size(); i = i + 3) {
			String type = list.get(i);
			String name = list.get(i + 1);
			// 根据字符串字面意思,获取类型
			params.add(new Param(new CodeType(type), name));
		}

		// 这里不再直接推导返回类型
		CtMethod method = new CtMethod(null, methodName, params);
		method.methodLines = LineUtils.getSubLines(lines, index);

		if (Constants.STATIC_SCOPE.equals(scope)) {
			clazz.staticMethods.add(method);
		} else if (Constants.CLASS_SCOPE.equals(scope)) {
			clazz.methods.add(method);
		}

		return method.methodLines.size() + 1;

	}

}