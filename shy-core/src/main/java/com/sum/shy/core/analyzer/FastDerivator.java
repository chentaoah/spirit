package com.sum.shy.core.analyzer;

import java.util.Map;

import com.sum.shy.core.api.Handler;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CodeType;
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

	public static Type getType(CtClass clazz, Stmt stmt) {
		for (Token token : stmt.tokens) {
			Type type = getType(clazz, token);
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
	public static Type getType(CtClass clazz, Token token) {

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

	private static String getArrayType(Token token) {
		// TODO Auto-generated method stub
		return "List<Object>";
	}

	private static String getMapType(Token token) {
		// TODO Auto-generated method stub
		return "Map<Object,Object>";
	}

	public static Type getReturnType(CtClass clazz, CtMethod method) {

		Object result = MethodResolver.resolve(clazz, method, false, new Handler() {
			@Override
			public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
				// 如果是返回语句
				if (stmt.isReturn()) {
					return getType(clazz, stmt);
				}
				return null;
			}
		});
		return result != null ? (CodeType) result : new CodeType("void");
	}

}
