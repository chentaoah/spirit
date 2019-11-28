package com.sum.shy.core.analyzer;

import java.util.List;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.CtClass;
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

	public static Type getType(CtClass clazz, Stmt stmt) {

		// 如果其中有==判断,则整个语句认为是判断语句
		for (Token token : stmt.tokens) {
			if (token.isOperator() && "==".equals(token.value))
				return new CodeType(clazz, "boolean");
		}
		// 其他类型,进行返回值推导
		for (Token token : stmt.tokens) {
			Type type = getType(clazz, token);
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
	public static Type getType(CtClass clazz, Token token) {

		if (token.isType()) {// 类型声明
			return new CodeType(clazz, token);// 转换成type token

		} else if (token.isCast()) {// 类型强制转换
			return new CodeType(clazz, token.getTypeNameAtt());// 转换成type token

		} else if (token.isArrayInit()) {// 数组初始化
			return new CodeType(clazz, token.getTypeNameAtt());// 转换成type token

		} else if (token.isValue()) {// 字面值
			return getValueType(clazz, token);// 转换成type token

		} else if (token.isVariable()) {// 变量
			if (token.isVar() && token.getTypeAtt() != null) {// 单纯的变量就向上追溯到有用的
				return token.getTypeAtt();
			}
			return token.getReturnTypeAtt();

		} else if (token.isInvoke()) {// 方法调用
			// 如果不存在下一个，则可以直接返回了
			if (token.getNext() == null)
				return token.getReturnTypeAtt();

		} else if (token.isQuickIndex()) {
			// 如果不存在下一个，则可以直接返回了
			if (token.getNext() == null)
				return token.getReturnTypeAtt();

		}

		return null;
	}

	private static Type getValueType(CtClass clazz, Token token) {
		if (token.isNull()) {
			return new CodeType(clazz, "Object");
		} else if (token.isBool()) {
			return new CodeType(clazz, "boolean");
		} else if (token.isInt()) {
			return new CodeType(clazz, "int");
		} else if (token.isDouble()) {
			return new CodeType(clazz, "double");
		} else if (token.isStr()) {
			return new CodeType(clazz, "String");
		} else if (token.isArray()) {
			return getArrayType(clazz, token);
		} else if (token.isMap()) {
			return getMapType(clazz, token);
		}
		return null;
	}

	private static Type getArrayType(CtClass clazz, Token token) {
		boolean isSame = true;// 所有元素是否都相同
		Type finalType = null;
		// 开始遍历
		Stmt stmt = (Stmt) token.value;
		for (Stmt subStmt : stmt.split(",")) {
			Type type = getType(clazz, subStmt);
			if (type != null) {// 如果有个类型,不是最终类型的话,则直接
				if (finalType != null) {
					if (!finalType.toString().equals(type.toString())) {// 如果存在多个类型
						isSame = false;
						break;
					}
				} else {
					finalType = type;
				}
			}
		}

		// 1.如果集合中已经明显存在多个类型的元素,那就直接返回Object,不用再推导了
		// 2.可能是个空的集合
		if (!isSame || finalType == null)
			return new CodeType(clazz, "List<Object>");

		return new CodeType(clazz, String.format("List<%s>", getWrapType(finalType.toString())));
	}

	private static Type getMapType(CtClass clazz, Token token) {
		boolean isSameKey = true;
		boolean isSameValue = true;
		Type finalKeyType = null;
		Type finalValueType = null;
		Stmt stmt = (Stmt) token.value;
		for (Stmt subStmt : stmt.split(",")) {
			List<Stmt> subStmts = subStmt.split(":");
			Type KeyType = getType(clazz, subStmts.get(0));
			Type valueType = getType(clazz, subStmts.get(1));
			if (KeyType != null) {// 如果有个类型,不是最终类型的话,则直接
				if (finalKeyType != null) {
					if (!finalKeyType.toString().equals(KeyType.toString())) {// 如果存在多个类型
						isSameKey = false;
						break;
					}
				} else {
					finalKeyType = KeyType;
				}
			}
			if (valueType != null) {// 如果有个类型,不是最终类型的话,则直接
				if (finalValueType != null) {
					if (!finalValueType.toString().equals(valueType.toString())) {// 如果存在多个类型
						isSameValue = false;
						break;
					}
				} else {
					finalValueType = valueType;
				}
			}
		}
		// 类型不相同,或者是空的map,则取Object类型
		String key = !isSameKey || finalKeyType == null ? "Object" : getWrapType(finalKeyType.toString());
		String value = !isSameValue || finalValueType == null ? "Object" : getWrapType(finalValueType.toString());
		return new CodeType(clazz, String.format("Map<%s, %s>", key, value));

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
