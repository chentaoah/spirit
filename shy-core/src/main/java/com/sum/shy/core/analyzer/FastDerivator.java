package com.sum.shy.core.analyzer;

import com.sum.shy.core.api.Handler;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
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
	 * 尽量给出包含type token的codeType,如果无法给出,则返回一个函数表达式,或者数据结构本身
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
			if (token.getTypeAtt() != null) {// 这个变量必须有类型才能够被返回
				if (token.isVar()) {// 单纯的变量就向上追溯到有用的
					return token.getTypeAtt();
				}
				return new CodeType(token);
			}

		} else if (token.isInvoke()) {// 方法调用
			if (token.isInvokeInit()) {// 构造方法
				return new CodeType(token.getTypeNameAtt());// 转换成type token
			}
			return new CodeType(token);

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
			return new CodeType(getArrayType(token));
		} else if (token.isMap()) {
			return new CodeType(getMapType(token));
		}
		return null;
	}

	private static Token getArrayType(Token token) {
		Type type = getTypeByStep(token, 0, 1);
		// 如果为null,则说明还需要进行深度推导,那么将该集合直接返回
		return type != null ? new Token(Constants.TYPE_TOKEN, "List<" + getWrapType(type.getTypeName()) + ">", null)
				: token;
	}

	private static Token getMapType(Token token) {
		Type firstType = getTypeByStep(token, 1, 4);
		Type secondType = getTypeByStep(token, 3, 4);
		// 如果为null,则说明还需要进行深度推导,那么将该集合直接返回
		return firstType != null && secondType != null
				? new Token(Constants.TYPE_TOKEN,
						"Map<" + getWrapType(firstType.getTypeName()) + "," + getWrapType(secondType.getTypeName())
								+ ">",
						null)
				: token;

	}

	/**
	 * 根据一定的格式,跳跃式的获取到集合中的泛型参数
	 * 
	 * @param token
	 * @param step
	 */
	public static Type getTypeByStep(Token token, int start, int step) {

		boolean isSame = true;// 所有元素是否都相同
		boolean isFinal = true;// 所有元素都是最终结果
		Type finalType = null;
		Stmt subStmt = (Stmt) token.value;
		for (int i = start; i < subStmt.size(); i = i + step) {
			Token subToken = subStmt.getToken(i);
			Type type = getType(subToken);
			if (type != null) {// 如果有个类型,不是最终类型的话,则直接
				if (type.isFinal()) {
					if (finalType != null) {
						if (!finalType.getTypeName().equals(type.getTypeName())) {// 如果存在多个类型
							isSame = false;
							break;
						}
					} else {
						finalType = type;
					}
				} else {
					isFinal = false;
				}
			}
		}

		// 1.如果集合中已经明显存在多个类型的元素,那就直接返回Object,不用再推导了
		if (!isSame)
			return new CodeType("Object");
		// 2.如果集合中的元素类型一致,其中还有需要进行推导的元素,则返回null
		if (!isFinal)
			return null;
		// 3.如果类型都相同,且无须推导,那么直接返回该类型
		return finalType;

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

	public static Type getReturnType(CtClass clazz, CtMethod method) {

		Object result = MethodResolver.resolve(clazz, method, false, new Handler() {
			@Override
			public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
				// 如果是返回语句
				if (stmt.isReturn()) {
					return getType(stmt);
				}
				return null;
			}
		});
		return result != null ? (CodeType) result : new CodeType("void");
	}

}
