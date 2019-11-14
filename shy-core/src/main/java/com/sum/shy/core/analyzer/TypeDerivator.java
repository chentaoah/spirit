package com.sum.shy.core.analyzer;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class TypeDerivator {

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static Type getType(Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			Type type = getType(token);
			if (type != null) {
				return type;
			}

		}
		return null;
	}

	public static Type getType(Token token) {

		if (token.isBool()) {
			return new CodeType("bool");

		} else if (token.isInt()) {
			return new CodeType("int");

		} else if (token.isDouble()) {
			return new CodeType("double");

		} else if (token.isStr()) {
			return new CodeType("str");

		} else if (token.isArray()) {
			return new CodeType(getArrayGenericType(token) + "[]");

		} else if (token.isMap()) {
			return new CodeType("map<" + getMapGenericTypes(token) + ">");

		} else if (token.isCast()) {
			return token.getTypeAtt();

		} else if (token.isVar()) {
			return token.getTypeAtt();// 这个返回可能是null,比如赋值语句的第一个变量

		} else if (token.isInvoke()) {// 如果是方法调用,则直接返回返回类型
			return getFluentReturnType(token);// 如果是fluent调用，则返回最终的返回类型

		} else if (token.isStaticVar()) {
			return token.getReturnTypeAtt();

		} else if (token.isMemberVar()) {
			return token.getReturnTypeAtt();
		}
		return null;
	}

	private static String getArrayGenericType(Token token) {
		Stmt subStmt = (Stmt) token.value;
		Type type = getType(subStmt);
		if (type == null) {
			return "obj";
		} else {
			return type.getName();
		}
	}

	private static String getMapGenericTypes(Token token) {
		// TODO Auto-generated method stub
		return null;
	}

//	private static Map<String, NativeType> getMapGenericTypes(Token token) {
//		Map<String, NativeType> genericTypes = new LinkedHashMap<>();
//		Stmt subStmt = (Stmt) token.value;
//		if (subStmt.size() == 2) {// 如果map里面没有元素
//			genericTypes.put("K", new NativeType(Object.class));
//			genericTypes.put("V", new NativeType(Object.class));
//		} else {
//			// 开始遍历里面的那层
//			boolean flag = true;
//			NativeType firstType = null;
//			NativeType secondType = null;
//			for (Token subToken : subStmt.tokens) {
//				if (":".equals(subToken.value)) {
//					flag = false;
//				} else if (",".equals(subToken.value)) {
//					flag = true;
//				}
//				NativeType subType = (NativeType) getType(subToken);
//				if (subType != null) {
//					if (flag) {
//						firstType = subType;
//					} else {
//						secondType = subType;
//					}
//				}
//			}
//			genericTypes.put("K", firstType);
//			genericTypes.put("V", secondType);
//		}
//
//		return genericTypes;
//	}
//
	private static Type getFluentReturnType(Token token) {
		Type type = token.getReturnTypeAtt();
		Token nextToken = token;
		do {
			nextToken = nextToken.getNextTokenAtt();
			if (nextToken != null && nextToken.isFluent()) {
				type = nextToken.getReturnTypeAtt();
			}

		} while (nextToken != null);
		return type;
	}

}
