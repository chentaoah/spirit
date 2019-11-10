package com.sum.shy.core.analyzer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.NativeType;

public class TypeDerivator {

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static NativeType getNativeType(Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			NativeType type = getType(token);
			if (type != null) {
				return type;
			}

		}
		return null;
	}

	public static NativeType getType(Token token) {

		if (token.isBoolean()) {
			return new NativeType(boolean.class);

		} else if (token.isInt()) {
			return new NativeType(int.class);

		} else if (token.isDouble()) {
			return new NativeType(double.class);

		} else if (token.isStr()) {
			return new NativeType(String.class);

		} else if (token.isArray()) {
			return new NativeType(List.class, getArrayGenericTypes(token));

		} else if (token.isMap()) {
			return new NativeType(Map.class, getMapGenericTypes(token));

		} else if (token.isCast()) {
			return token.getNativeTypeAtt();

		} else if (token.isVar()) {
			return token.getNativeTypeAtt();// 这个返回可能是null,比如赋值语句的第一个变量

		} else if (token.isInvoke()) {// 如果是方法调用,则直接返回返回类型
			return getFluentReturnType(token);// 如果是fluent调用，则返回最终的返回类型

		} else if (token.isMemberVar()) {
			return token.getReturnNativeTypeAtt();
		}
		return null;
	}

	private static Map<String, NativeType> getArrayGenericTypes(Token token) {
		Map<String, NativeType> genericTypes = new LinkedHashMap<>();
		Stmt subStmt = (Stmt) token.value;
		if (subStmt.size() == 2) {// 如果数组里面没有元素
			genericTypes.put("E", new NativeType(Object.class));
		} else {
			genericTypes.put("E", getNativeType(subStmt));
		}
		return genericTypes;
	}

	private static Map<String, NativeType> getMapGenericTypes(Token token) {
		Map<String, NativeType> genericTypes = new LinkedHashMap<>();
		Stmt subStmt = (Stmt) token.value;
		if (subStmt.size() == 2) {// 如果map里面没有元素
			genericTypes.put("K", new NativeType(Object.class));
			genericTypes.put("V", new NativeType(Object.class));
		} else {
			// 开始遍历里面的那层
			boolean flag = true;
			NativeType firstType = null;
			NativeType secondType = null;
			for (Token subToken : subStmt.tokens) {
				if (":".equals(subToken.value)) {
					flag = false;
				} else if (",".equals(subToken.value)) {
					flag = true;
				}
				NativeType subType = getType(subToken);
				if (subType != null) {
					if (flag) {
						firstType = subType;
					} else {
						secondType = subType;
					}
				}
			}
			genericTypes.put("K", firstType);
			genericTypes.put("V", secondType);
		}

		return genericTypes;
	}

	private static NativeType getFluentReturnType(Token token) {
		NativeType nativeType = token.getReturnNativeTypeAtt();
		Token nextToken = token;
		do {
			nextToken = nextToken.getNextTokenAtt();
			if (nextToken != null && nextToken.isFluent()) {
				nativeType = nextToken.getReturnNativeTypeAtt();
			}

		} while (nextToken != null);
		return nativeType;
	}

}
