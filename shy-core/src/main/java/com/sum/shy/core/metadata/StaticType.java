package com.sum.shy.core.metadata;

import com.sum.shy.core.clazz.IType;

public class StaticType {

	public static final IType WILDCARD_TYPE = new IType();
	public static final IType BOOLEAN_TYPE = new IType();
	public static final IType INT_TYPE = new IType();
	public static final IType LONG_TYPE = new IType();
	public static final IType DOUBLE_TYPE = new IType();
	public static final IType OBJECT_TYPE = new IType();
	public static final IType STRING_TYPE = new IType();

	static {

		WILDCARD_TYPE.setClassName(null);
		WILDCARD_TYPE.setSimpleName(null);
		WILDCARD_TYPE.setTypeName(null);
		WILDCARD_TYPE.setPrimitive(false);
		WILDCARD_TYPE.setArray(false);
		WILDCARD_TYPE.setGenericTypes(null);
		WILDCARD_TYPE.setWildcard(true);
		WILDCARD_TYPE.setDeclarer(null);
		WILDCARD_TYPE.setNative(false);

		BOOLEAN_TYPE.setClassName(null);
		BOOLEAN_TYPE.setSimpleName(null);
		BOOLEAN_TYPE.setTypeName(null);
		BOOLEAN_TYPE.setPrimitive(false);
		BOOLEAN_TYPE.setArray(false);
		BOOLEAN_TYPE.setGenericTypes(null);
		BOOLEAN_TYPE.setWildcard(true);
		BOOLEAN_TYPE.setDeclarer(null);
		BOOLEAN_TYPE.setNative(false);

		INT_TYPE.setClassName(null);
		INT_TYPE.setSimpleName(null);
		INT_TYPE.setTypeName(null);
		INT_TYPE.setPrimitive(false);
		INT_TYPE.setArray(false);
		INT_TYPE.setGenericTypes(null);
		INT_TYPE.setWildcard(true);
		INT_TYPE.setDeclarer(null);
		INT_TYPE.setNative(false);

		LONG_TYPE.setClassName(null);
		LONG_TYPE.setSimpleName(null);
		LONG_TYPE.setTypeName(null);
		LONG_TYPE.setPrimitive(false);
		LONG_TYPE.setArray(false);
		LONG_TYPE.setGenericTypes(null);
		LONG_TYPE.setWildcard(true);
		LONG_TYPE.setDeclarer(null);
		LONG_TYPE.setNative(false);

		DOUBLE_TYPE.setClassName(null);
		DOUBLE_TYPE.setSimpleName(null);
		DOUBLE_TYPE.setTypeName(null);
		DOUBLE_TYPE.setPrimitive(false);
		DOUBLE_TYPE.setArray(false);
		DOUBLE_TYPE.setGenericTypes(null);
		DOUBLE_TYPE.setWildcard(true);
		DOUBLE_TYPE.setDeclarer(null);
		DOUBLE_TYPE.setNative(false);

		OBJECT_TYPE.setClassName(null);
		OBJECT_TYPE.setSimpleName(null);
		OBJECT_TYPE.setTypeName(null);
		OBJECT_TYPE.setPrimitive(false);
		OBJECT_TYPE.setArray(false);
		OBJECT_TYPE.setGenericTypes(null);
		OBJECT_TYPE.setWildcard(true);
		OBJECT_TYPE.setDeclarer(null);
		OBJECT_TYPE.setNative(false);

		STRING_TYPE.setClassName(null);
		STRING_TYPE.setSimpleName(null);
		STRING_TYPE.setTypeName(null);
		STRING_TYPE.setPrimitive(false);
		STRING_TYPE.setArray(false);
		STRING_TYPE.setGenericTypes(null);
		STRING_TYPE.setWildcard(true);
		STRING_TYPE.setDeclarer(null);
		STRING_TYPE.setNative(false);
	}

}
