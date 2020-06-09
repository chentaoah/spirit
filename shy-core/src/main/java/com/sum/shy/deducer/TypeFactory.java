package com.sum.shy.deducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.common.Context;
import com.sum.shy.common.StaticType;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;
import com.sum.shy.lexical.SemanticDelegate;
import com.sum.shy.lib.Assert;
import com.sum.shy.processor.FastDeducer;
import com.sum.shy.utils.TypeUtils;

public class TypeFactory {

	public static IType create(String className) {// 一般来说，className可以直接反应出大部分属性
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(TypeUtils.getSimpleName(className));
		type.setTypeName(TypeUtils.getTypeName(className));
		type.setPrimitive(TypeUtils.isPrimitive(className));
		type.setArray(TypeUtils.isArray(className));
		type.setNull(false);
		type.setWildcard(false);
		type.setNative(!Context.get().contains(TypeUtils.getTargetName(className)));
		return type;
	}

	public static IType create(Class<?> clazz) {
		return create(clazz.getName());
	}

	public static IType create(Class<?> clazz, List<IType> genericTypes) {
		IType type = create(clazz);
		type.setGenericTypes(genericTypes);
		return type;
	}

	public static IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, SemanticDelegate.getToken(text));
	}

	public static IType create(IClass clazz, Token token) {
		if (token.isType()) {
			IType type = new IType();
			if (token.value instanceof String) {// String // String[] //? //T,K
				String simpleName = (String) token.value;
				if ("?".equals(simpleName)) {// 未知类型
					return StaticType.WILDCARD_TYPE;

				} else {
					type = clazz.getTypeVariable(simpleName);// 泛型参数
					if (type == null)
						type = create(clazz.findImport(simpleName));// 一般类型
				}
			} else if (token.value instanceof Stmt) {// List<String> // Class<?>
				Stmt stmt = token.getStmt();
				String simpleName = stmt.getStr(0);// 前缀
				type = create(clazz.findImport(simpleName));
				type.setGenericTypes(getGenericTypes(clazz, stmt));
			}
			return type;

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			return create(clazz, token.getSimpleNameAtt());

		} else if (token.isValue()) {// 1, 1.1, "xxxx"
			return getValueType(clazz, token);
		}
		return null;
	}

	public static List<IType> getGenericTypes(IClass clazz, Stmt stmt) {
		List<IType> genericTypes = new ArrayList<>();
		for (int i = 1; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isType())
				genericTypes.add(create(clazz, token));
		}
		return genericTypes;
	}

	public static IType getValueType(IClass clazz, Token token) {
		if (token.isBool()) {
			return StaticType.BOOLEAN_TYPE;
		} else if (token.isChar()) {
			return StaticType.CHAR_TYPE;
		} else if (token.isInt()) {
			return StaticType.INT_TYPE;
		} else if (token.isLong()) {
			return StaticType.LONG_TYPE;
		} else if (token.isDouble()) {
			return StaticType.DOUBLE_TYPE;
		} else if (token.isNull()) {
			return StaticType.NULL_TYPE;
		} else if (token.isStr()) {
			return StaticType.STRING_TYPE;
		} else if (token.isList()) {
			return getListType(clazz, token);
		} else if (token.isMap()) {
			return getMapType(clazz, token);
		}
		return null;
	}

	public static IType getListType(IClass clazz, Token token) {
		Stmt stmt = token.getStmt();
		List<Stmt> stmts = stmt.subStmt(1, stmt.size() - 1).split(",");
		IType type = create(List.class);
		type.getGenericTypes().add(getGenericType(clazz, stmts));
		return type;
	}

	public static IType getMapType(IClass clazz, Token token) {
		Stmt stmt = token.getStmt();
		List<Stmt> keyStmts = new ArrayList<>();
		List<Stmt> valueStmts = new ArrayList<>();
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			List<Stmt> subStmts = subStmt.split(":");
			keyStmts.add(subStmts.get(0));
			valueStmts.add(subStmts.get(1));
		}
		IType type = create(Map.class);
		type.getGenericTypes().add(getGenericType(clazz, keyStmts));
		type.getGenericTypes().add(getGenericType(clazz, valueStmts));
		return type;
	}

	public static IType getGenericType(IClass clazz, List<Stmt> stmts) {

		if (stmts.size() == 0)
			return StaticType.OBJECT_TYPE;

		IType genericType = null;
		for (Stmt subStmt : stmts) {
			IType wrappedType = FastDeducer.deriveStmt(clazz, subStmt).getWrappedType();
			if (genericType == null) {
				genericType = wrappedType;
				continue;
			}
			if (wrappedType.isMatch(genericType)) {// 更抽象则替换
				genericType = wrappedType;

			} else if (!genericType.isMatch(wrappedType)) {// 不同则使用Object
				genericType = StaticType.OBJECT_TYPE;
				break;
			}
		}
		Assert.notNull(genericType, "Generic type cannot be null!");
		return genericType;
	}

}