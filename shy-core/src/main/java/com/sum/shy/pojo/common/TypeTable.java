package com.sum.shy.pojo.common;

import com.sum.shy.pojo.clazz.IType;

public class TypeTable {

	public static final IType VOID_TYPE;
	public static final IType BOOLEAN_TYPE;
	public static final IType CHAR_TYPE;
	public static final IType BYTE_TYPE;
	public static final IType SHORT_TYPE;
	public static final IType INT_TYPE;
	public static final IType LONG_TYPE;
	public static final IType FLOAT_TYPE;
	public static final IType DOUBLE_TYPE;

	public static final IType BOOLEAN_ARRAY_TYPE;
	public static final IType CHAR_ARRAY_TYPE;
	public static final IType BYTE_ARRAY_TYPE;
	public static final IType SHORT_ARRAY_TYPE;
	public static final IType INT_ARRAY_TYPE;
	public static final IType LONG_ARRAY_TYPE;
	public static final IType FLOAT_ARRAY_TYPE;
	public static final IType DOUBLE_ARRAY_TYPE;

	public static final IType VOID_WRAPPED_TYPE;
	public static final IType BOOLEAN_WRAPPED_TYPE;
	public static final IType CHAR_WRAPPED_TYPE;
	public static final IType BYTE_WRAPPED_TYPE;
	public static final IType SHORT_WRAPPED_TYPE;
	public static final IType INT_WRAPPED_TYPE;
	public static final IType LONG_WRAPPED_TYPE;
	public static final IType FLOAT_WRAPPED_TYPE;
	public static final IType DOUBLE_WRAPPED_TYPE;

	public static final IType OBJECT_TYPE;
	public static final IType STRING_TYPE;
	public static final IType OBJECT_ARRAY_TYPE;
	public static final IType STRING_ARRAY_TYPE;

	public static final IType NULL_TYPE;
	public static final IType WILDCARD_TYPE;

	static {
		VOID_TYPE = build("void", "void", "void", true/* primitive */, false, false, false, false);
		BOOLEAN_TYPE = build("boolean", "boolean", "boolean", true/* primitive */, false, false, false, false);
		CHAR_TYPE = build("char", "char", "char", true/* primitive */, false, false, false, false);
		BYTE_TYPE = build("byte", "byte", "byte", true/* primitive */, false, false, false, false);
		SHORT_TYPE = build("short", "short", "short", true/* primitive */, false, false, false, false);
		INT_TYPE = build("int", "int", "int", true/* primitive */, false, false, false, false);
		LONG_TYPE = build("long", "long", "long", true/* primitive */, false, false, false, false);
		FLOAT_TYPE = build("float", "float", "float", true/* primitive */, false, false, false, false);
		DOUBLE_TYPE = build("double", "double", "double", true/* primitive */, false, false, false, false);

		BOOLEAN_ARRAY_TYPE = build("[Z", "boolean[]", "boolean[]", false, true/* array */, false, false, false);
		CHAR_ARRAY_TYPE = build("[C", "char[]", "char[]", false, true/* array */, false, false, false);
		BYTE_ARRAY_TYPE = build("[B", "byte[]", "byte[]", false, true/* array */, false, false, false);
		SHORT_ARRAY_TYPE = build("[S", "short[]", "short[]", false, true/* array */, false, false, false);
		INT_ARRAY_TYPE = build("[I", "int[]", "int[]", false, true/* array */, false, false, false);
		LONG_ARRAY_TYPE = build("[J", "long[]", "long[]", false, true/* array */, false, false, false);
		FLOAT_ARRAY_TYPE = build("[F", "float[]", "float[]", false, true/* array */, false, false, false);
		DOUBLE_ARRAY_TYPE = build("[D", "double[]", "double[]", false, true/* array */, false, false, false);

		VOID_WRAPPED_TYPE = build("java.lang.Void", "Void", "java.lang.Void", false, false, false, false, true/* native */);
		BOOLEAN_WRAPPED_TYPE = build("java.lang.Boolean", "Boolean", "java.lang.Boolean", false, false, false, false, true/* native */);
		CHAR_WRAPPED_TYPE = build("java.lang.Character", "Character", "java.lang.Character", false, false, false, false, true/* native */);
		BYTE_WRAPPED_TYPE = build("java.lang.Byte", "Byte", "java.lang.Byte", false, false, false, false, true/* native */);
		SHORT_WRAPPED_TYPE = build("java.lang.Short", "Short", "java.lang.Short", false, false, false, false, true/* native */);
		INT_WRAPPED_TYPE = build("java.lang.Integer", "Integer", "java.lang.Integer", false, false, false, false, true/* native */);
		LONG_WRAPPED_TYPE = build("java.lang.Long", "Long", "java.lang.Long", false, false, false, false, true/* native */);
		FLOAT_WRAPPED_TYPE = build("java.lang.Float", "Float", "java.lang.Float", false, false, false, false, true/* native */);
		DOUBLE_WRAPPED_TYPE = build("java.lang.Double", "Double", "java.lang.Double", false, false, false, false, true/* native */);

		OBJECT_TYPE = build("java.lang.Object", "Object", "java.lang.Object", false, false, false, false, true/* native */);
		STRING_TYPE = build("java.lang.String", "String", "java.lang.String", false, false, false, false, true/* native */);
		OBJECT_ARRAY_TYPE = build("[Ljava.lang.Object;", "Object[]", "java.lang.Object[]", false, true/* array */, false, false, true/* native */);
		STRING_ARRAY_TYPE = build("[Ljava.lang.String;", "String[]", "java.lang.String[]", false, true/* array */, false, false, true/* native */);

		NULL_TYPE = build("java.lang.Object", "Object", "java.lang.Object", false, false, true/* null */, false, true/* native */);
		WILDCARD_TYPE = build("java.lang.Object", "Object", "java.lang.Object", false, false, false, true/* wildcard */, true/* native */);
	}

	public static IType build(String className, String simpleName, String typeName, boolean isPrimitive, boolean isArray, boolean isNull, boolean isWildcard,
			boolean isNative) {
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
		type.setModifiers(IType.PUBLIC_MODIFIERS);
		type.setGenericTypes(null);
		return type;
	}

	public static IType getWrappedType(String className) {

		if (BOOLEAN_TYPE.getClassName().equals(className)) {
			return BOOLEAN_WRAPPED_TYPE;

		} else if (CHAR_TYPE.getClassName().equals(className)) {
			return CHAR_WRAPPED_TYPE;

		} else if (BYTE_TYPE.getClassName().equals(className)) {
			return BYTE_WRAPPED_TYPE;

		} else if (SHORT_TYPE.getClassName().equals(className)) {
			return SHORT_WRAPPED_TYPE;

		} else if (INT_TYPE.getClassName().equals(className)) {
			return INT_WRAPPED_TYPE;

		} else if (LONG_TYPE.getClassName().equals(className)) {
			return LONG_WRAPPED_TYPE;

		} else if (FLOAT_TYPE.getClassName().equals(className)) {
			return FLOAT_WRAPPED_TYPE;

		} else if (DOUBLE_TYPE.getClassName().equals(className)) {
			return DOUBLE_WRAPPED_TYPE;
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		Class<?>[] classes = new Class[] { boolean.class, boolean[].class, char.class, char[].class, short.class, short[].class, int.class, int[].class,
				long.class, long[].class, float.class, float[].class, double.class, double[].class, byte.class, byte[].class, Object.class, Object[].class,
				String.class, String[].class };
		for (Class<?> clazz : classes) {
			System.out.println("=== " + clazz.getSimpleName() + ".class ===");
			System.out.println(clazz.getName());
			System.out.println(clazz.getSimpleName());
			System.out.println(clazz.getTypeName());
			System.out.println(clazz.isPrimitive());
			System.out.println(clazz.isArray());
		}
	}
}
