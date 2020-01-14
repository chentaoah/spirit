package com.sum.shy.parser;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.Param;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.lexical.LexicalAnalyzer;
import com.sum.shy.parser.api.Parser;
import com.sum.shy.type.CodeType;
import com.sum.shy.utils.CuttingUtils;
import com.sum.shy.utils.LineUtils;

public class FuncParser implements Parser {

	@Override
	public int parse(IClass clazz, String scope, List<Line> lines, int index, Line line, Stmt stmt) {

		// 注解
		List<String> annotations = Context.get().getAnnotations();
		// 是否同步
		boolean isSync = false;
		// 方法名
		String methodName = null;
		// 参数
		List<Param> params = new ArrayList<>();
		// 异常
		List<String> exceptions = new ArrayList<>();
		// func sync methodName(int num) throws exception
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isKeyword()) {
				if (Constants.FUNC_KEYWORD.equals(token.toString())) {
					Token nextToken = stmt.getToken(i + 1);
					if (Constants.SYNC_KEYWORD.equals(nextToken.toString())) {
						isSync = true;
						nextToken = stmt.getToken(i + 2);
					}
					// methodName(int num)
					String methodDesc = nextToken.toString();
					// 方法名
					methodName = CuttingUtils.getPrefix(methodDesc);
					// 参数
					String content = CuttingUtils.getContent(methodDesc);
					// 拆分
					List<String> list = LexicalAnalyzer.getWords(content);
					for (int j = 0; j < list.size(); j = j + 3) {
						String type = list.get(j);
						String name = list.get(j + 1);
						// 根据字符串字面意思,获取类型
						params.add(new Param(new CodeType(clazz, type), name));
					}
				} else if (Constants.THROWS_KEYWORD.equals(token.toString())) {
					for (int j = i + 1; j < stmt.size(); j++) {
						Token nextToken = stmt.getToken(j);
						if (nextToken.isKeywordParam()) {
							exceptions.add(nextToken.toString());
						}
					}
				}
			}
		}

		// 这里不再直接推导返回类型
		IMethod method = new IMethod(annotations, scope, isSync, null, methodName, params, exceptions);
		method.methodLines = LineUtils.getSubLines(lines, index);
		clazz.addMethod(method);

		return method.methodLines.size() + 1;

	}

}