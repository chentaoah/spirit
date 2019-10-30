package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class TypeDerivator {

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static String getTypeByStmt(Stmt stmt) {
		return getType(stmt.tokens);
	}

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static String getTypeByWords(List<String> words) {
		return getType(SemanticDelegate.getTokens(words));
	}

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static String getType(List<Token> tokens) {
		for (Token token : tokens) {
			String type = token.type;
			if ("boolean".equals(type)) {
				return "boolean";
			} else if ("int".equals(type)) {
				return "int";
			} else if ("double".equals(type)) {
				return "double";
			} else if ("str".equals(type)) {
				return "str";
			} else if (type.startsWith("invoke_init")) {
				return token.attachments.get("init_method_name");
			} else if ("array".equals(type)) {
				return "array";
			} else if ("map".equals(type)) {
				return "map";
			}
		}
		return "unknown";
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
			String type = token.type;
			if ("array".equals(type)) {
				// 获取子语句
				Stmt subStmt = (Stmt) token.value;
				// 开始遍历里面的那层
				for (Token subToken : subStmt.tokens) {
					String subType = getGenericType(subToken);
					if (!"unknown".equals(subType)) {
						genericTypes.add(subType);
						return genericTypes;
					}
				}
			} else if ("map".equals(type)) {
				// 获取子语句
				Stmt subStmt = (Stmt) token.value;
				// 开始遍历里面的那层
				boolean flag = true;
				genericTypes.add("unknown");
				genericTypes.add("unknown");
				for (Token subToken : subStmt.tokens) {
					if (":".equals(subToken.value)) {
						flag = false;
					} else if (",".equals(subToken.value)) {
						flag = true;
					}
					String subType = getGenericType(subToken);
					if (!"unknown".equals(subType)) {
						genericTypes.set(flag ? 0 : 1, subType);
						if (!"unknown".equals(genericTypes.get(0)) && !"unknown".equals(genericTypes.get(1))) {
							return genericTypes;
						}
					}
				}
			}
		}
		return genericTypes;
	}

	public static String getGenericType(Token subToken) {
		String subType = subToken.type;
		if ("boolean".equals(subType)) {
			return "boolean";
		} else if ("int".equals(subType)) {
			return "int";
		} else if ("double".equals(subType)) {
			return "double";
		} else if ("str".equals(subType)) {
			return "str";
		} else if ("invoke_init".equals(subType)) {
			return subToken.attachments.get("init_method_name");
		}
		return "unknown";
	}

}
