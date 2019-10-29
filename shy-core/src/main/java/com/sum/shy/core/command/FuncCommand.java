package com.sum.shy.core.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.api.Command;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Result;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.utils.LineUtils;

public class FuncCommand implements Command {

	@Override
	public Result analysis(List<String> lines, int index, String line, String syntax, List<String> units) {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public int handle(String scope, Clazz clazz, List<String> lines, int index, Stmt stmt) {
//		// 如果是在根域下,则开始解析
//		if ("static".equals(scope)) {
//			return createMethod(clazz, clazz.staticMethods, lines, index, stmt);
//		} else if ("class".equals(scope)) {
//			return createMethod(clazz, clazz.methods, lines, index, stmt);
//		}
//		return 0;
//	}
//
//	private int createMethod(Clazz clazz, List<Method> methods, List<String> lines, int index, Stmt stmt) {
//
//		String str = stmt.getUnit(1);
//		// 这里一定要trim一下
//		List<String> list = Splitter.on(CharMatcher.anyOf("(,)")).trimResults().splitToList(str);
//		// 方法名
//		String name = list.get(0);
//		// 开始遍历参数
//		List<Param> params = new ArrayList<>();
//		for (int i = 1; i < list.size(); i++) {
//			// 可能是user.say()无参数方法
//			if (list.get(i).length() > 0) {
//				String[] strs = list.get(i).split(" ");
//				params.add(new Param(strs[0], strs[1]));
//			}
//		}
//		// 创建方法
//		Method method = new Method(null, name, params);
//		methods.add(method);
//		method.methodLines = LineUtils.getSubLines(lines, index);
//
//		// 寻找return
//		String returnType = "var";
//		for (int i = 0; i < method.methodLines.size(); i++) {
//			Stmt methodSentence = new Stmt(method.methodLines.get(i));
//			if ("return".equals(methodSentence.getUnit(0))) {
//				returnType = Analyzer.getType(clazz.defTypes, methodSentence);
//			}
//		}
//		method.returnType = returnType;
//
//		return method.methodLines.size() + 1;
//
//	}
}