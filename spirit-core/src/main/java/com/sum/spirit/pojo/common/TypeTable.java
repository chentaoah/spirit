package com.sum.spirit.pojo.common;

import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.link.TypeAdapter;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.utils.ReflectUtils;
import com.sum.spirit.utils.TypeUtils;

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

	public static TypeAdapter adapter = ProxyFactory.get(TypeAdapter.class);

	static {
		VOID_TYPE = build("void", "void", "void", true/* primitive */, false, false, false);
		BOOLEAN_TYPE = build("boolean", "boolean", "boolean", true/* primitive */, false, false, false);
		CHAR_TYPE = build("char", "char", "char", true/* primitive */, false, false, false);
		BYTE_TYPE = build("byte", "byte", "byte", true/* primitive */, false, false, false);
		SHORT_TYPE = build("short", "short", "short", true/* primitive */, false, false, false);
		INT_TYPE = build("int", "int", "int", true/* primitive */, false, false, false);
		LONG_TYPE = build("long", "long", "long", true/* primitive */, false, false, false);
		FLOAT_TYPE = build("float", "float", "float", true/* primitive */, false, false, false);
		DOUBLE_TYPE = build("double", "double", "double", true/* primitive */, false, false, false);

		BOOLEAN_ARRAY_TYPE = build("[Z", "boolean[]", "boolean[]", false, true/* array */, false, false);
		CHAR_ARRAY_TYPE = build("[C", "char[]", "char[]", false, true/* array */, false, false);
		BYTE_ARRAY_TYPE = build("[B", "byte[]", "byte[]", false, true/* array */, false, false);
		SHORT_ARRAY_TYPE = build("[S", "short[]", "short[]", false, true/* array */, false, false);
		INT_ARRAY_TYPE = build("[I", "int[]", "int[]", false, true/* array */, false, false);
		LONG_ARRAY_TYPE = build("[J", "long[]", "long[]", false, true/* array */, false, false);
		FLOAT_ARRAY_TYPE = build("[F", "float[]", "float[]", false, true/* array */, false, false);
		DOUBLE_ARRAY_TYPE = build("[D", "double[]", "double[]", false, true/* array */, false, false);

		VOID_WRAPPED_TYPE = build("spirit.lang.Void", "Void", "spirit.lang.Void", false, false, false, false);
		BOOLEAN_WRAPPED_TYPE = build("spirit.lang.Boolean", "Boolean", "spirit.lang.Boolean", false, false, false, false);
		CHAR_WRAPPED_TYPE = build("spirit.lang.Character", "Character", "spirit.lang.Character", false, false, false, false);
		BYTE_WRAPPED_TYPE = build("spirit.lang.Byte", "Byte", "spirit.lang.Byte", false, false, false, false);
		SHORT_WRAPPED_TYPE = build("spirit.lang.Short", "Short", "spirit.lang.Short", false, false, false, false);
		INT_WRAPPED_TYPE = build("spirit.lang.Integer", "Integer", "spirit.lang.Integer", false, false, false, false);
		LONG_WRAPPED_TYPE = build("spirit.lang.Long", "Long", "spirit.lang.Long", false, false, false, false);
		FLOAT_WRAPPED_TYPE = build("spirit.lang.Float", "Float", "spirit.lang.Float", false, false, false, false);
		DOUBLE_WRAPPED_TYPE = build("spirit.lang.Double", "Double", "spirit.lang.Double", false, false, false, false);

		OBJECT_TYPE = build("spirit.lang.Object", "Object", "spirit.lang.Object", false, false, false, false);
		STRING_TYPE = build("spirit.lang.String", "String", "spirit.lang.String", false, false, false, false);
		OBJECT_ARRAY_TYPE = build("[Lspirit.lang.Object;", "Object[]", "spirit.lang.Object[]", false, true/* array */, false, false);
		STRING_ARRAY_TYPE = build("[Lspirit.lang.String;", "String[]", "spirit.lang.String[]", false, true/* array */, false, false);

		NULL_TYPE = build("spirit.lang.Object", "Object", "spirit.lang.Object", false, false, true/* null */, false);
		WILDCARD_TYPE = build("spirit.lang.Object", "Object", "spirit.lang.Object", false, false, false, true/* wildcard */);
	}

	public static IType build(String className, String simpleName, String typeName, boolean isPrimitive, boolean isArray, boolean isNull, boolean isWildcard) {
		IType type = new IType();
		type.setClassName(className);
		type.setSimpleName(simpleName);
		type.setTypeName(typeName);
		type.setGenericName(null);
		type.setPrimitive(isPrimitive);
		type.setArray(isArray);
		type.setNull(isNull);
		type.setWildcard(isWildcard);
		type.setNative(false);
		type.setModifiers(IType.PUBLIC_MODIFIERS);
		type.setGenericTypes(null);
		// convert the type to native type
		if (adapter != null)
			return adapter.adapte(type);
		return type;
	}

	public static boolean isPrimitive(String className) {

		if (VOID_TYPE.getClassName().equals(className)) {
			return true;

		} else if (BOOLEAN_TYPE.getClassName().equals(className)) {
			return true;

		} else if (CHAR_TYPE.getClassName().equals(className)) {
			return true;

		} else if (BYTE_TYPE.getClassName().equals(className)) {
			return true;

		} else if (SHORT_TYPE.getClassName().equals(className)) {
			return true;

		} else if (INT_TYPE.getClassName().equals(className)) {
			return true;

		} else if (LONG_TYPE.getClassName().equals(className)) {
			return true;

		} else if (FLOAT_TYPE.getClassName().equals(className)) {
			return true;

		} else if (DOUBLE_TYPE.getClassName().equals(className)) {
			return true;
		}
		return false;
	}

	public static String getPrimitive(String className) {
		return isPrimitive(className) ? className : null;
	}

	public static IType getWrappedType(String className) {

		if (VOID_TYPE.getClassName().equals(className)) {
			return VOID_WRAPPED_TYPE;

		} else if (BOOLEAN_TYPE.getClassName().equals(className)) {
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

	public static String getTargetName(String className) {

		if (BOOLEAN_ARRAY_TYPE.getClassName().equals(className)) {
			return BOOLEAN_TYPE.getClassName();

		} else if (CHAR_ARRAY_TYPE.getClassName().equals(className)) {
			return CHAR_TYPE.getClassName();

		} else if (BYTE_ARRAY_TYPE.getClassName().equals(className)) {
			return BYTE_TYPE.getClassName();

		} else if (SHORT_ARRAY_TYPE.getClassName().equals(className)) {
			return SHORT_TYPE.getClassName();

		} else if (INT_ARRAY_TYPE.getClassName().equals(className)) {
			return INT_TYPE.getClassName();

		} else if (LONG_ARRAY_TYPE.getClassName().equals(className)) {
			return LONG_TYPE.getClassName();

		} else if (FLOAT_ARRAY_TYPE.getClassName().equals(className)) {
			return FLOAT_TYPE.getClassName();

		} else if (DOUBLE_ARRAY_TYPE.getClassName().equals(className)) {
			return DOUBLE_TYPE.getClassName();
		}
		return null;
	}

	public static String getClassName(String simpleName) {

		String className = getPrimitive(simpleName);
		if (StringUtils.isNotEmpty(className))
			return className;

		if (BOOLEAN_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return BOOLEAN_ARRAY_TYPE.getClassName();

		} else if (CHAR_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return CHAR_ARRAY_TYPE.getClassName();

		} else if (BYTE_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return BYTE_ARRAY_TYPE.getClassName();

		} else if (SHORT_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return SHORT_ARRAY_TYPE.getClassName();

		} else if (INT_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return INT_ARRAY_TYPE.getClassName();

		} else if (LONG_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return LONG_ARRAY_TYPE.getClassName();

		} else if (FLOAT_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return FLOAT_ARRAY_TYPE.getClassName();

		} else if (DOUBLE_ARRAY_TYPE.getSimpleName().equals(simpleName)) {
			return DOUBLE_ARRAY_TYPE.getClassName();
		}

		className = ReflectUtils.getClassName(TypeUtils.getTargetName(simpleName), TypeUtils.isArray(simpleName));

		return StringUtils.isNotEmpty(className) ? className : null;
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
