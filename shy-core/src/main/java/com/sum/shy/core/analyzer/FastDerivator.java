package com.sum.shy.core.analyzer;

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

		if (token.isValue()) {
			return new CodeType(token);

		} else if (token.isInvoke()) {
			return new CodeType(token);

		} else if (token.isType()) {
			return new CodeType(token);

		} else if (token.isCast()) {
			return new CodeType(token);

		}

		return null;
	}

	public static Type getReturnType(Clazz clazz, Method method) {

		List<Line> lines = method.methodLines;
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;

			Stmt stmt = Stmt.create(line);
			VariableTracker.check(clazz, method, "0", line, stmt);

			if (stmt.isAssignment()) {// 如果是赋值语句
				Type type = getType(clazz, stmt);
				method.addVariable(new Variable("0", type, stmt.get(0)));

			} else if (stmt.isReturn()) {// 如果是返回语句
				return getType(clazz, stmt);
			}
		}

		return null;
	}

}
