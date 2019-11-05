package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

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
	public static String getType(Stmt stmt) {
		return getType(stmt.tokens);
	}

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static String getType(List<Token> tokens) {
		for (Token token : tokens) {
			if (token.isBoolean()) {
				return Constants.BOOLEAN_TYPE;
			} else if (token.isInt()) {
				return Constants.INT_TYPE;
			} else if (token.isDouble()) {
				return Constants.DOUBLE_TYPE;
			} else if (token.isStr()) {
				return Constants.STR_TYPE;
			} else if (token.isArray()) {
				return Constants.ARRAY_TYPE;
			} else if (token.isMap()) {
				return Constants.MAP_TYPE;
			} else if (token.isInvokeInit()) {// 构造函数
				return token.getInitMethodNameAttachment();
			} else if (token.isVar()) {
				String type = token.getTypeAttachment();
				if (type != null) {
					return type;
				}
			}
		}
		return Constants.UNKNOWN;
	}

	/**
	 * 泛型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static List<String> getGenericTypes(Stmt stmt) {
		List<String> genericTypes = new ArrayList<>();
		// 开始遍历最外层,看下是否是array或者map
		for (Token token : stmt.tokens) {
			if (token.isArray()) {
				// 获取子语句
				Stmt subStmt = (Stmt) token.value;
				// 如果数组里面什么都没有,则返回null
				if (subStmt.size() == 2) {
					genericTypes.add(Constants.NONE);
					return genericTypes;
				}
				genericTypes.add(Constants.UNKNOWN);
				// 开始遍历里面的那层
				for (Token subToken : subStmt.tokens) {
					String subType = getGenericType(subToken);
					if (!Constants.UNKNOWN.equals(subType)) {
						genericTypes.set(0, subType);
						return genericTypes;
					}
				}
			} else if (token.isMap()) {
				// 获取子语句
				Stmt subStmt = (Stmt) token.value;
				// 如果数组里面什么都没有,则返回null
				if (subStmt.size() == 2) {
					genericTypes.add(Constants.NONE);
					genericTypes.add(Constants.NONE);
					return genericTypes;
				}
				// 开始遍历里面的那层
				boolean flag = true;
				genericTypes.add(Constants.UNKNOWN);
				genericTypes.add(Constants.UNKNOWN);
				for (Token subToken : subStmt.tokens) {
					if (":".equals(subToken.value)) {
						flag = false;
					} else if (",".equals(subToken.value)) {
						flag = true;
					}
					String subType = getGenericType(subToken);
					if (!Constants.UNKNOWN.equals(subType)) {
						genericTypes.set(flag ? 0 : 1, subType);
						if (!Constants.UNKNOWN.equals(genericTypes.get(0))
								&& !Constants.UNKNOWN.equals(genericTypes.get(1))) {
							return genericTypes;
						}
					}
				}
			} else if (token.isVar()) {
				// 从变量的附加参数里面取
				List<String> genericTypesAttachment = token.getGenericTypesAttachment();
				if (genericTypesAttachment != null && genericTypesAttachment.size() > 0) {
					return genericTypesAttachment;
				}
			}
		}
		return genericTypes;
	}

	public static String getGenericType(Token subToken) {

		if (subToken.isBoolean()) {
			return Constants.BOOLEAN_TYPE;
		} else if (subToken.isInt()) {
			return Constants.INT_TYPE;
		} else if (subToken.isDouble()) {
			return Constants.DOUBLE_TYPE;
		} else if (subToken.isStr()) {
			return Constants.STR_TYPE;
		} else if (subToken.isInvokeInit()) {
			return subToken.getInitMethodNameAttachment();
		} else if (subToken.isVar()) {
			String type = subToken.getTypeAttachment();
			if (type != null) {
				return type;
			}
		}
		return Constants.UNKNOWN;
	}

}
