package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

/**
 * 快速推导器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年11月15日
 */
public class FastDerivator {

	public static Type getType(Clazz clazz, Stmt stmt) {
		for (Token token : stmt.tokens) {
			Type type = getType(clazz, token);
			if (type != null) {
				return type;
			}
		}
		return null;
	}

	public static Type getType(Clazz clazz, Token token) {

		if (token.isType()) {// 类型声明
			return new CodeType(token);

		} else if (token.isCast()) {// 类型强制转换
			return new CodeType(token);

		} else if (token.isValue()) {// 字面值
			return new CodeType(token);

		} else if (token.isInvoke()) {// 方法调用
			return new CodeType(token);

		} else if (token.isVariable()) {// 变量
			// 这个变量必须有类型才能够被返回
			if (token.getTypeAtt() != null) {
				return new CodeType(token);
			}

		}

		return null;
	}

	public static Type getReturnType(Clazz clazz, Method method) {

		int depth = 0;
		List<Integer> counts = new ArrayList<>();
		counts.add(1);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);
		counts.add(0);

		List<Line> lines = method.methodLines;
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;

			Stmt stmt = Stmt.create(line);
			// 判断是否进入子块
			if ("}".equals(stmt.frist())) {
				depth--;
			}
			if ("{".equals(stmt.last())) {
				depth++;
				counts.set(depth, counts.get(depth) + 1);
			}
			StringBuilder sb = new StringBuilder();
			for (Integer count : counts) {
				if (count == 0)
					break;
				sb.append(count + "-");
			}

			VariableTracker.track(clazz, method, sb.toString(), line, stmt);

			if (stmt.isAssignment()) {// 如果是赋值语句
				Type type = getType(clazz, stmt);
				method.addVariable(new Variable(sb.toString(), type, stmt.get(0)));

			} else if (stmt.isReturn()) {// 如果是返回语句
				method.variables.clear();// 返回前,清理掉所有的变量
				return getType(clazz, stmt);
			}
		}
		method.variables.clear();// 返回前,清理掉所有的变量
		return null;
	}

}
