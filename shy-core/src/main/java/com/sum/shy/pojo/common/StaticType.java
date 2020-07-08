package com.sum.shy.pojo.common;

import com.sum.shy.pojo.clazz.IType;

public class StaticType {

	public static final IType VOID_TYPE;
	public static final IType BOOLEAN_TYPE;
	public static final IType CHAR_TYPE;
	public static final IType INT_TYPE;
	public static final IType LONG_TYPE;
	public static final IType DOUBLE_TYPE;
	public static final IType OBJECT_TYPE;
	public static final IType NULL_TYPE;
	public static final IType WILDCARD_TYPE;
	public static final IType STRING_TYPE;

	static {
		VOID_TYPE = build("void", "void", "void", null, true, false, false, false, false);
		BOOLEAN_TYPE = build("boolean", "boolean", "boolean", null, true, false, false, false, false);
		CHAR_TYPE = build("char", "char", "char", null, true, false, false, false, false);
		INT_TYPE = build("int", "int", "int", null, true, false, false, false, false);
		LONG_TYPE = build("long", "long", "long", null, true, false, false, false, false);
		DOUBLE_TYPE = build("double", "double", "double", null, true, false, false, false, false);
		OBJECT_TYPE = build("java.lang.Object", "Object", "java.lang.Object", null, false, false, false, false, false);
		NULL_TYPE = build("java.lang.Object", "Object", "java.lang.Object", null, false, false, true/* null */, false, false);
		WILDCARD_TYPE = build("java.lang.Object", "Object", "java.lang.Object", null, false, false, false, true/* wildcard */, false);
		STRING_TYPE = build("java.lang.String", "String", "java.lang.String", null, false, false, false, false, true/* native */);
	}

	public static IType build(String className, String simpleName, String typeName, String genericName, boolean isPrimitive, boolean isArray, boolean isNull,
			boolean isWildcard, boolean isNative) {
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(simpleName);
		type.setTypeName(typeName);
		type.setGenericName(genericName);
		type.setPrimitive(isPrimitive);
		type.setArray(isArray);
		type.setNull(isNull);
		type.setWildcard(isWildcard);
		type.setNative(isNative);
		type.setModifiers(IType.PUBLIC_MODIFIERS);
		type.setGenericTypes(null);
		return type;
	}

}
