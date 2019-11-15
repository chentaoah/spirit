package com.sum.shy.core.analyzer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
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

		Type type = getBasicType(clazz, token);

		if (type != null)
			return type;

		type = getObjectType(clazz, token);

		return type;
	}

	private static Type getBasicType(Clazz clazz, Token token) {

		if (token.isNull()) {
			return new CodeType("obj", null, null, null, null, null);

		} else if (token.isBool()) {
			return new CodeType("bool", null, null, null, null, null);

		} else if (token.isInt()) {
			return new CodeType("int", null, null, null, null, null);

		} else if (token.isDouble()) {
			return new CodeType("double", null, null, null, null, null);

		} else if (token.isStr()) {
			return new CodeType("str", null, null, null, null, null);

		} else if (token.isArray()) {
			return new CodeType("array", getArrayGenericType(clazz, token), null, null, null, null);

		} else if (token.isMap()) {
			return new CodeType("map", getMapGenericTypes(clazz, token), null, null, null, null);

		}
		return null;
	}

	private static Type getObjectType(Clazz clazz, Token token) {

		if (token.isCast()) {
			String type = token.getClassNameAtt();
			String className = clazz.findImport(type);
			return new CodeType(className, getGenericTypes(type), null, null, null, null);

		} else if (token.isVar()) {
			return token.getTypeAtt();

		} else if (token.isStaticVar()) {
			String type = token.getClassNameAtt();// 类名
			String className = clazz.findImport(type);
			List<String> varNames = token.getVarNamesAtt();
			return new CodeType(className, getGenericTypes(type), null, varNames, null, null);

		} else if (token.isMemberVar()) {
			Type type = token.getTypeAtt();
			List<String> varNames = token.getVarNamesAtt();
			type.next(new CodeType(null, null, null, varNames, null, null));
			return type;

		} else if (token.isInvoke()) {
			return getInvokeType(clazz, token);

		}
		return null;
	}

	private static Type getInvokeType(Clazz clazz, Token token) {

		if (token.isInvokeInit()) {// 构造方法
			String type = token.getMethodNameAtt();
			String className = clazz.findImport(type);
			return new CodeType(className, getGenericTypes(type), null, null, null, null);

		} else if (token.isInvokeStatic()) {// 静态调用
			String type = token.getClassNameAtt();
			String className = clazz.findImport(type);
			return new CodeType(className, getGenericTypes(type), null, null, null, null);

		} else if (token.isInvokeMember()) {// 成员方法调用
			Type type = token.getTypeAtt();
			List<String> varNames = token.getVarNamesAtt();
			String methodName = token.getMethodNameAtt();
			type.next(new CodeType(null, null, null, varNames, methodName, null));
			return type;

		} else if (token.isInvokeFluent()) {// 流式方法调用
			// TODO 暂时没有实现

		} else if (token.isInvokeLocal()) {// 本地方法调用
			// TODO 暂时没有实现
		}
		return null;

	}

	private static Map<String, CodeType> getGenericTypes(String type) {
		Map<String, CodeType> map = new LinkedHashMap<>();
		List<String> list = Splitter.on(CharMatcher.anyOf("<,>")).omitEmptyStrings().trimResults().splitToList(type);
		for (int i = 1; i < list.size(); i++) {
			map.put(String.valueOf(i - 1), new CodeType(list.get(i), null, null, null, null, null));
		}
		return map;
	}

	private static Map<String, CodeType> getArrayGenericType(Clazz clazz, Token token) {
		Map<String, CodeType> map = new LinkedHashMap<>();
		Stmt subStmt = (Stmt) token.value;
		if (subStmt.size() == 2) {
			map.put("E", new CodeType("obj", null, null, null, null, null));
		} else {
			Type subType = getType(clazz, subStmt);
			map.put("E", (CodeType) subType);
		}
		return map;
	}

	private static Map<String, CodeType> getMapGenericTypes(Clazz clazz, Token token) {
		Map<String, CodeType> map = new LinkedHashMap<>();
		Stmt subStmt = (Stmt) token.value;
		if (subStmt.size() == 2) {
			map.put("K", new CodeType("obj", null, null, null, null, null));
			map.put("V", new CodeType("obj", null, null, null, null, null));
		} else {
			boolean flag = true;
			for (Token subToken : subStmt.tokens) {
				if (":".equals(subToken.value)) {
					flag = false;
				} else if (",".equals(subToken.value)) {
					flag = true;
				}
				Type subType = getType(clazz, subToken);
				if (subType != null) {
					if (flag) {
						map.put("K", (CodeType) subType);
					} else {
						map.put("V", (CodeType) subType);
					}
					// 中断
					if (map.size() == 2)
						break;
				}
			}
		}
		return map;

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

		return new CodeType("void", null, null, null, null, null);
	}

}
