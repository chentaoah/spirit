package com.sum.shy.core.deduce;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.type.CodeType;
import com.sum.shy.type.api.Type;
import com.sum.shy.utils.ReflectUtils;

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

	public static Type deriveStmt(IClass clazz, Stmt stmt) {
		for (Token token : stmt.tokens) {
			if (token.getTypeAtt() != null) {// 如果有类型直接返回
				return token.getTypeAtt();

			} else if (token.isNode()) {// 如果是节点，则推导节点的类型
				return getType(clazz, token.getNode());

			}
		}
		return null;
	}

	public static Type getType(IClass clazz, Node node) {

		if (node == null)
			return null;

		Token token = node.token;
		// 如果是逻辑判断，或者类型判断关键字
		if (token.isLogicalOperator() || token.isJudgeOperator() || token.isInstanceof()) {
			return new CodeType(clazz, Constants.BOOLEAN_TYPE);

		} else if (token.isCalcOperator()) {
			// 先取左边的，再取右边的
			if (node.left != null) {
				return getType(clazz, node.left);
			} else if (node.right != null) {
				return getType(clazz, node.right);
			}
		}

		return token.getTypeAtt();

	}

	public static Type getValueType(IClass clazz, Token token) {
		if (token.isBool()) {
			return new CodeType(clazz, Constants.BOOLEAN_TYPE);
		} else if (token.isInt()) {
			return new CodeType(clazz, Constants.INT_TYPE);
		} else if (token.isDouble()) {
			return new CodeType(clazz, Constants.DOUBLE_TYPE);
		} else if (token.isNull()) {
			return new CodeType(clazz, Constants.OBJECT_TYPE);
		} else if (token.isStr()) {
			return new CodeType(clazz, Constants.STRING_TYPE);
		} else if (token.isList()) {
			return getArrayType(clazz, token);
		} else if (token.isMap()) {
			return getMapType(clazz, token);
		}
		return null;
	}

	public static Type getArrayType(IClass clazz, Token token) {
		boolean isSame = true;// 所有元素是否都相同
		Type genericType = null;
		// 开始遍历
		Stmt stmt = token.getSubStmt();
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			Type type = deriveStmt(clazz, subStmt);
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

		Type finalType = new CodeType(clazz, Constants.LIST_TYPE);
		finalType.getGenericTypes().add(getWrapType(clazz, genericType));
		return finalType;
	}

	public static Type getMapType(IClass clazz, Token token) {
		boolean isSameKey = true;
		boolean isSameValue = true;
		Type finalKeyType = null;
		Type finalValueType = null;
		Stmt stmt = token.getSubStmt();
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			List<Stmt> subStmts = subStmt.split(":");
			Type KeyType = deriveStmt(clazz, subStmts.get(0));
			Type valueType = deriveStmt(clazz, subStmts.get(1));
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
		finalKeyType = !isSameKey || finalKeyType == null ? new CodeType(clazz, Constants.OBJECT_TYPE) : finalKeyType;
		finalValueType = !isSameValue || finalValueType == null ? new CodeType(clazz, Constants.OBJECT_TYPE)
				: finalValueType;

		Type finalType = new CodeType(clazz, Constants.MAP_TYPE);
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
	public static Type getWrapType(IClass clazz, Type genericType) {
		String wrapType = ReflectUtils.getWrapType(genericType.getClassName());
		if (wrapType != null)
			genericType = new CodeType(clazz, wrapType);
		return genericType;
	}

}
