package com.sum.shy.core.analyzer;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

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

	public static Type getType(Stmt stmt) {
		for (Token token : stmt.tokens) {
			Type type = getType(token);
			if (type != null) {
				return type;
			}
		}
		return null;
	}

	/**
	 * 这里只会返回确定的类型,需要推导的都只是返回null
	 * 
	 * @param clazz
	 * @param token
	 * @return
	 */
	public static Type getType(Token token) {

		if (token.isType()) {// 类型声明
			return new CodeType(token);// 转换成type token

		} else if (token.isCast()) {// 类型强制转换
			return new CodeType(token.getTypeNameAtt());// 转换成type token

		} else if (token.isValue()) {// 字面值
			return getValueType(token);// 转换成type token

		} else if (token.isVariable()) {// 变量
			if (token.isVar() && token.getTypeAtt() != null) {// 单纯的变量就向上追溯到有用的
				return token.getTypeAtt();
			}
			return token.getReturnTypeAtt();

		} else if (token.isInvoke()) {// 方法调用
			return token.getReturnTypeAtt();
		}

		return null;
	}

	private static Type getValueType(Token token) {
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
			return getArrayType(token);
		} else if (token.isMap()) {
			return getMapType(token);
		}
		return null;
	}

	private static Type getArrayType(Token token) {
		Type type = getTypeByStep(token, 0, 1);
		return type != null ? new CodeType("List<" + getWrapType(type.getTypeName()) + ">") : null;
	}

	private static Type getMapType(Token token) {
		Type firstType = getTypeByStep(token, 1, 4);
		Type secondType = getTypeByStep(token, 3, 4);
		return firstType != null && secondType != null
				? new CodeType("Map<" + getWrapType(firstType.getTypeName()) + ","
						+ getWrapType(secondType.getTypeName()) + ">")
				: null;

	}

	/**
	 * 根据一定的格式,跳跃式的获取到集合中的泛型参数
	 * 
	 * @param token
	 * @param step
	 */
	public static Type getTypeByStep(Token token, int start, int step) {

		boolean isSame = true;// 所有元素是否都相同
		Type finalType = null;
		Stmt subStmt = (Stmt) token.value;
		for (int i = start; i < subStmt.size(); i = i + step) {
			Token subToken = subStmt.getToken(i);
			Type type = getType(subToken);
			if (type != null) {// 如果有个类型,不是最终类型的话,则直接
				if (finalType != null) {
					if (!finalType.getTypeName().equals(type.getTypeName())) {// 如果存在多个类型
						isSame = false;
						break;
					}
				} else {
					finalType = type;
				}
			}
		}

		// 1.如果集合中已经明显存在多个类型的元素,那就直接返回Object,不用再推导了
		if (!isSame)
			return new CodeType("Object");
		// 2.可能是个空的集合
		return finalType != null ? finalType : new CodeType("Object");

	}

	public static String getWrapType(String typeName) {
		switch (typeName) {
		case "boolean":
			return "Boolean";
		case "int":
			return "Integer";
		case "long":
			return "Long";
		case "double":
			return "Double";
		default:
			return typeName;
		}
	}

}
