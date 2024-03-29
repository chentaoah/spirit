package com.gitee.spirit.core.clazz.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.gitee.spirit.common.enums.AccessLevelEnum;
import com.gitee.spirit.common.enums.PrimitiveEnum;
import com.gitee.spirit.core.clazz.entity.IType;

public class TypeBuilder {

	public static IType build(String className, String simpleName, String typeName, boolean isPrimitive, boolean isArray, boolean isNull, boolean isWildcard, boolean isNative) {
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(simpleName);
		type.setTypeName(typeName);
		type.setGenericName(null);
		type.setPrimitive(isPrimitive);
		type.setArray(isArray);
		type.setNull(isNull);
		type.setWildcard(isWildcard);
		type.setNative(isNative);
		type.setModifiers(AccessLevelEnum.PUBLIC.value);
		type.setGenericTypes(new ArrayList<>());
		return type;
	}

	public static IType build(PrimitiveEnum primitiveEnum) {
		IType type = new IType();
		type.setClassName(primitiveEnum.className);
		type.setSimpleName(primitiveEnum.simpleName);
		type.setTypeName(primitiveEnum.typeName);
		type.setGenericName(null);
		type.setPrimitive(primitiveEnum.isPrimitive);
		type.setArray(primitiveEnum.isArray);
		type.setNull(false);
		type.setWildcard(false);
		type.setNative(false);
		type.setModifiers(AccessLevelEnum.PUBLIC.value);
		type.setGenericTypes(new ArrayList<>());
		return type;
	}

	public static IType copy(IType oldType) {
		IType newType = new IType();
		newType.setClassName(oldType.getClassName());
		newType.setSimpleName(oldType.getSimpleName());
		newType.setTypeName(oldType.getTypeName());
		newType.setGenericName(oldType.getGenericName());
		newType.setPrimitive(oldType.isPrimitive());
		newType.setArray(oldType.isArray());
		newType.setNull(oldType.isNull());
		newType.setWildcard(oldType.isWildcard());
		newType.setNative(oldType.isNative());
		newType.setModifiers(oldType.getModifiers());
		newType.setGenericTypes(Collections.unmodifiableList(oldType.getGenericTypes()));
		return newType;
	}

}
