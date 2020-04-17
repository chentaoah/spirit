package com.sum.shy.core.clazz.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.metadata.StaticType;
import com.sum.shy.core.processor.FastDeducer;
import com.sum.shy.core.utils.TypeUtils;
import com.sum.shy.lib.Assert;

public class TypeFactory {

	public static IType create(String className) {// 一般来说，className可以直接反应出大部分属性
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(TypeUtils.getSimpleName(className));
		type.setTypeName(TypeUtils.getTypeName(className));
		type.setPrimitive(TypeUtils.isPrimitive(className));
		type.setArray(TypeUtils.isArray(className));
		type.setGenericTypes(null);
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
			if (token.value instanceof String) {// String // String[] //?
				String simpleName = (String) token.value;
				if ("?".equals(simpleName)) {// 未知类型
					type = StaticType.WILDCARD_TYPE;

				} else {// 一般类型
					type = create(clazz.findImport(simpleName));
				}

			} else if (token.value instanceof Stmt) {// List<String> // Class<?>
				Stmt subStmt = token.getStmt();
				String simpleName = subStmt.getStr(0);// 前缀
				type = create(clazz.findImport(simpleName));
				type.setGenericTypes(getGenericTypes(clazz, subStmt));
			}
			return type;

		} else if (token.isArrayInit() || token.isTypeInit() || token.isCast()) {
			return create(clazz, token.getSimpleNameAtt());

		} else if (token.isValue()) {// 1, 1.1, "xxxx"
			return getValueType(clazz, token);
		}
		return null;
	}

	private static List<IType> getGenericTypes(IClass clazz, Stmt subStmt) {
		List<IType> genericTypes = new ArrayList<>();
		for (int i = 1; i < subStmt.size(); i++) {
			Token subToken = subStmt.getToken(i);
			if (subToken.isType())
				genericTypes.add(create(clazz, subToken));
		}
		return genericTypes;
	}

	public static IType getValueType(IClass clazz, Token token) {
		if (token.isBool()) {
			return StaticType.BOOLEAN_TYPE;
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
		IType currentType = null;
		Stmt stmt = token.getStmt();
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			IType wrappedType = FastDeducer.deriveStmt(clazz, subStmt).getWrappedType();
			if (currentType == null) {
				currentType = wrappedType;
				continue;
			}
			if (wrappedType.isMatch(currentType)) {// 更抽象则替换
				currentType = wrappedType;

			} else if (!currentType.isMatch(wrappedType)) {// 不同则使用Object
				currentType = StaticType.OBJECT_TYPE;
				break;
			}
		}
		IType type = create(List.class);
		type.getGenericTypes().add(currentType);
		return type;
	}

	public static IType getMapType(IClass clazz, Token token) {
		boolean isSameKey = true;
		boolean isSameValue = true;
		IType finalKeyType = null;
		IType finalValueType = null;
		Stmt stmt = token.getStmt();
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			List<Stmt> subStmts = subStmt.split(":");
			IType KeyType = FastDeducer.deriveStmt(clazz, subStmts.get(0));
			IType valueType = FastDeducer.deriveStmt(clazz, subStmts.get(1));
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
		finalKeyType = !isSameKey || finalKeyType == null ? StaticType.OBJECT_TYPE : finalKeyType;
		finalValueType = !isSameValue || finalValueType == null ? StaticType.OBJECT_TYPE : finalValueType;

		IType finalType = create(Map.class);
		finalType.getGenericTypes().add(finalKeyType.getWrappedType());
		finalType.getGenericTypes().add(finalValueType.getWrappedType());
		return finalType;

	}

}
