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
			return new CodeType(token);// 转换成type token

		} else if (token.isCast()) {// 类型强制转换
			return new CodeType(token.getTypeNameAtt());// 转换成type token

		} else if (token.isValue()) {// 字面值
			return getValueCodeType(token);// 转换成type token

		} else if (token.isVariable()) {// 变量
			if (token.getTypeAtt() != null) {// 这个变量必须有类型才能够被返回
				return new CodeType(token);
			}

		} else if (token.isInvoke()) {// 方法调用
			if (token.isInvokeInit()) {// 构造方法
				return new CodeType(token.getTypeNameAtt());// 转换成type token
			}
			return new CodeType(token);

		}

		return null;
	}

	private static Type getValueCodeType(Token token) {
		if (token.isNull()) {
			return new CodeType("Object");
		} else if (token.isBool()) {
			return new CodeType("boolean");
		} else if (token.isInt()) {
			return new CodeType("int");
		} else if (token.isDouble()) {
			return new CodeType("double");
		} else if (token.isStr()) {
			return new CodeType("String");
		} else if (token.isArray()) {
			return new CodeType("List");
		} else if (token.isMap()) {
			return new CodeType("Map");
		}
		return null;
	}

	public static Type getReturnType(Clazz clazz, Method method) {

		int depth = 0;
		// 这里默认给了八级的深度
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
			boolean ignore = false;
			if ("}".equals(stmt.frist())) {
				depth--;
				ignore = true;
			}
			if ("{".equals(stmt.last())) {
				depth++;
				counts.set(depth, counts.get(depth) + 1);
				ignore = true;
			}
			if (ignore)
				continue;

			StringBuilder sb = new StringBuilder();
			for (Integer count : counts) {
				if (count == 0)
					break;
				sb.append(count + "-");
			}
			String block = sb.toString();

			VariableTracker.track(clazz, method, block, line, stmt);
			// 如果是赋值语句
			if (stmt.isAssignment()) {
				// 判断变量追踪是否帮我们找到了该变量的类型
				Token token = stmt.getToken(0);
				// 如果没有找到,则进行推导
				if (token.isVar() && token.getTypeAtt() == null) {
					Type type = getType(clazz, stmt);
					method.addVariable(new Variable(block, type, stmt.get(0)));
				}
			} else if (stmt.isReturn()) {// 如果是返回语句
				method.variables.clear();// 返回前,清理掉所有的变量
				return getType(clazz, stmt);
			}
		}
		method.variables.clear();// 返回前,清理掉所有的变量
		return new CodeType("void");
	}

}
