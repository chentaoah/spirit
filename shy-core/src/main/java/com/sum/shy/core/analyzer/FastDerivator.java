package com.sum.shy.core.analyzer;

import java.util.List;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.ReflectUtils;

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
			if (token.isJudgeOperator()) {
				return new CodeType(clazz, "boolean");
			} else if (token.isKeyword() && "instanceof".equals(token.value)) {
				return new CodeType(clazz, "boolean");
			}
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
		Type genericType = null;
		// 开始遍历
		Stmt stmt = (Stmt) token.value;
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			Type type = getType(clazz, subStmt);
			if (type != null) {// 如果有个类型,不是最终类型的话,则直接
				if (genericType != null) {
					if (!genericType.equals(type)) {// 如果存在多个类型
						isSame = false;
						break;
					}
				} else {
					genericType = type;
				}
			}
		}

		// 1.如果集合中已经明显存在多个类型的元素,那就直接返回Object,不用再推导了
		// 2.可能是个空的集合
		if (!isSame || genericType == null)
			return new CodeType(clazz, "List<Object>");

		Type finalType = new CodeType(clazz, "List");
		finalType.getGenericTypes().add(getWrapType(clazz, genericType));
		return finalType;
	}

	private static Type getMapType(CtClass clazz, Token token) {
		boolean isSameKey = true;
		boolean isSameValue = true;
		Type finalKeyType = null;
		Type finalValueType = null;
		Stmt stmt = (Stmt) token.value;
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			List<Stmt> subStmts = subStmt.split(":");
			Type KeyType = getType(clazz, subStmts.get(0));
			Type valueType = getType(clazz, subStmts.get(1));
			if (KeyType != null) {// 如果有个类型,不是最终类型的话,则直接
				if (finalKeyType != null) {
					if (!finalKeyType.equals(KeyType)) {// 如果存在多个类型
						isSameKey = false;
					}
				} else {
					finalKeyType = KeyType;
				}
			}
			if (valueType != null) {// 如果有个类型,不是最终类型的话,则直接
				if (finalValueType != null) {
					if (!finalValueType.equals(valueType)) {// 如果存在多个类型
						isSameValue = false;
					}
				} else {
					finalValueType = valueType;
				}
			}
		}
		// 类型不相同,或者是空的map,则取Object类型
		finalKeyType = !isSameKey || finalKeyType == null ? new CodeType(clazz, "Object") : finalKeyType;
		finalValueType = !isSameValue || finalValueType == null ? new CodeType(clazz, "Object") : finalValueType;

		Type finalType = new CodeType(clazz, "Map");
		finalType.getGenericTypes().add(getWrapType(clazz, finalKeyType));
		finalType.getGenericTypes().add(getWrapType(clazz, finalValueType));
		return finalType;

	}

	/**
	 * 获取封装类
	 * 
	 * @param clazz
	 * @param genericType
	 * @return
	 */
	private static Type getWrapType(CtClass clazz, Type genericType) {
		String wrapType = ReflectUtils.getWrapType(genericType.getClassName());
		if (wrapType != null)
			genericType = new CodeType(clazz, wrapType);
		return genericType;
	}

}
