package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Type;

public class TypeDerivator {

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static Type getType(Stmt stmt) {

		for (Token token : stmt.tokens) {
			Type type = getType(token);
			if (type != null)
				return type;
		}
		return new Type(Constants.UNKNOWN);
	}

	public static Type getType(Token token) {

		if (token.isBoolean()) {
			return new Type(Constants.BOOLEAN_TYPE);

		} else if (token.isInt()) {
			return new Type(Constants.INT_TYPE);

		} else if (token.isDouble()) {
			return new Type(Constants.DOUBLE_TYPE);

		} else if (token.isStr()) {
			return new Type(Constants.STR_TYPE);

		} else if (token.isArray()) {
			return new Type(Constants.ARRAY_TYPE, getArrayGenericTypes(token));

		} else if (token.isMap()) {
			return new Type(Constants.MAP_TYPE, getMapGenericTypes(token));

		} else if (token.isInvokeInit()) {// 构造函数
			return new Type(token.getInitMethodNameAtt());

		} else if (token.isCast()) {
			return token.getTypeAtt();

		} else if (token.isVar()) {
			return token.getTypeAtt();// 这个返回可能是null,比如赋值语句的第一个变量

		} else if (token.isInvoke()) {// 如果是方法调用,则直接返回返回类型
			return token.getReturnTypeAtt();
			
		} else if (token.isMemberVar()) {
			return token.getReturnTypeAtt();
		}
		return null;
	}

	private static List<Type> getArrayGenericTypes(Token token) {
		List<Type> genericTypes = new ArrayList<>();
		Stmt subStmt = (Stmt) token.value;
		if (subStmt.size() == 2) {// 如果数组里面没有元素
			genericTypes.add(new Type(Constants.OBJ_TYPE));
		} else {
			genericTypes.add(getType(subStmt));
		}
		return genericTypes;
	}

	private static List<Type> getMapGenericTypes(Token token) {
		List<Type> genericTypes = new ArrayList<>();
		Stmt subStmt = (Stmt) token.value;
		if (subStmt.size() == 2) {// 如果map里面没有元素
			genericTypes.add(new Type(Constants.OBJ_TYPE));
			genericTypes.add(new Type(Constants.OBJ_TYPE));
		} else {
			// 开始遍历里面的那层
			boolean flag = true;
			Type firstType = null;
			Type secondType = null;
			for (Token subToken : subStmt.tokens) {
				if (":".equals(subToken.value)) {
					flag = false;
				} else if (",".equals(subToken.value)) {
					flag = true;
				}
				Type subType = getType(subToken);
				if (subType != null) {
					if (flag) {
						firstType = subType;
					} else {
						secondType = subType;
					}
				}
			}
			genericTypes.add(firstType);
			genericTypes.add(secondType);
		}

		return genericTypes;
	}

}
