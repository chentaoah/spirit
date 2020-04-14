package com.sum.shy.core.clazz.type;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.metadata.StaticType;
import com.sum.shy.core.processor.FastDeducer;
import com.sum.shy.core.utils.ReflectUtils;
import com.sum.shy.core.utils.TypeUtils;

public class TypeFactory {

	public static final Pattern BASIC_TYPE_PATTERN = Pattern.compile("^(" + SemanticDelegate.BASIC_TYPE_ENUM + ")$");

	public static IType create(String className) {
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(TypeUtils.getSimpleName(className));
		type.setTypeName(TypeUtils.getTypeNameByClassName(className));
		type.setPrimitive(BASIC_TYPE_PATTERN.matcher(className).matches());
		type.setArray(TypeUtils.isArrayByClassName(className));
		type.setGenericTypes(null);
		type.setWildcard(false);
		type.setDeclarer(null);
		type.setNative(!Context.get().contains(className));
		return type;
	}

	public static IType create(IClass clazz, String text) {
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
					type.setClassName(clazz.findImport(simpleName));
					type.setSimpleName(simpleName);
					type.setTypeName(TypeUtils.getTypeName(simpleName));
					type.setPrimitive(BASIC_TYPE_PATTERN.matcher(type.getClassName()).matches());
					type.setArray(TypeUtils.isArray(simpleName));
					type.setGenericTypes(null);
					type.setWildcard(false);
					type.setDeclarer(clazz);
					type.setNative(!Context.get().contains(type.getClassName()));

				}
			} else if (token.value instanceof Stmt) {// List<String> // Class<?>
				Stmt subStmt = token.getStmt();
				String simpleName = subStmt.getStr(0);// 前缀
				type.setClassName(clazz.findImport(simpleName));
				type.setSimpleName(simpleName);
				type.setTypeName(simpleName);
				type.setPrimitive(false);
				type.setArray(false);
				type.setGenericTypes(getGenericTypes(clazz, subStmt));// 递归下去
				type.setWildcard(false);
				type.setDeclarer(clazz);
				type.setNative(!Context.get().contains(type.getClassName()));

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
			return StaticType.OBJECT_TYPE;
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
		boolean isSame = true;// 所有元素是否都相同
		IType genericType = null;
		// 开始遍历
		Stmt stmt = token.getStmt();
		for (Stmt subStmt : stmt.subStmt(1, stmt.size() - 1).split(",")) {
			IType type = FastDeducer.deriveStmt(clazz, subStmt);
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
			return create(clazz, "List<Object>");

		IType finalType = create(clazz, Constants.LIST);
		finalType.getGenericTypes().add(getWrapType(clazz, genericType));
		return finalType;
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
		finalKeyType = !isSameKey || finalKeyType == null ? create(clazz, Constants.OBJECT) : finalKeyType;
		finalValueType = !isSameValue || finalValueType == null ? create(clazz, Constants.OBJECT) : finalValueType;

		IType finalType = create(clazz, Constants.MAP);
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
	public static IType getWrapType(IClass clazz, IType genericType) {
		String wrapType = ReflectUtils.getWrapType(genericType.getClassName());
		if (wrapType != null)
			genericType = create(clazz, wrapType);
		return genericType;
	}

}
