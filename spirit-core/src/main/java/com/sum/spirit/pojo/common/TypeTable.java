package com.sum.spirit.pojo.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sum.spirit.api.lexer.SemanticParser;
import com.sum.spirit.api.link.NativeLoader;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.utils.SpringUtils;

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

	public static final IType CLASS_TYPE;
	public static final IType LIST_TYPE;
	public static final IType MAP_TYPE;

	public static final IType NULL_TYPE;
	public static final IType WILDCARD_TYPE;

	public static final Map<String, IType> PRIMITIVE_ARRAY_TARGET_MAPPING = new ConcurrentHashMap<>();
	public static final Map<String, IType> PRIMITIVE_ARRAY_MAPPING = new ConcurrentHashMap<>();

	static {
		VOID_TYPE = IType.build("void", "void", "void", true/* primitive */, false, false, false, false);
		BOOLEAN_TYPE = IType.build("boolean", "boolean", "boolean", true/* primitive */, false, false, false, false);
		CHAR_TYPE = IType.build("char", "char", "char", true/* primitive */, false, false, false, false);
		BYTE_TYPE = IType.build("byte", "byte", "byte", true/* primitive */, false, false, false, false);
		SHORT_TYPE = IType.build("short", "short", "short", true/* primitive */, false, false, false, false);
		INT_TYPE = IType.build("int", "int", "int", true/* primitive */, false, false, false, false);
		LONG_TYPE = IType.build("long", "long", "long", true/* primitive */, false, false, false, false);
		FLOAT_TYPE = IType.build("float", "float", "float", true/* primitive */, false, false, false, false);
		DOUBLE_TYPE = IType.build("double", "double", "double", true/* primitive */, false, false, false, false);

		BOOLEAN_ARRAY_TYPE = IType.build("[Z", "boolean[]", "boolean[]", false, true/* array */, false, false, false);
		CHAR_ARRAY_TYPE = IType.build("[C", "char[]", "char[]", false, true/* array */, false, false, false);
		BYTE_ARRAY_TYPE = IType.build("[B", "byte[]", "byte[]", false, true/* array */, false, false, false);
		SHORT_ARRAY_TYPE = IType.build("[S", "short[]", "short[]", false, true/* array */, false, false, false);
		INT_ARRAY_TYPE = IType.build("[I", "int[]", "int[]", false, true/* array */, false, false, false);
		LONG_ARRAY_TYPE = IType.build("[J", "long[]", "long[]", false, true/* array */, false, false, false);
		FLOAT_ARRAY_TYPE = IType.build("[F", "float[]", "float[]", false, true/* array */, false, false, false);
		DOUBLE_ARRAY_TYPE = IType.build("[D", "double[]", "double[]", false, true/* array */, false, false, false);

		// Get target type by array type
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[Z", BOOLEAN_TYPE);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[C", CHAR_TYPE);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[B", BYTE_TYPE);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[S", SHORT_TYPE);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[I", INT_TYPE);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[J", LONG_TYPE);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[F", FLOAT_TYPE);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[D", DOUBLE_TYPE);

		// Get primitive type by array type
		PRIMITIVE_ARRAY_MAPPING.put("boolean[]", BOOLEAN_ARRAY_TYPE);
		PRIMITIVE_ARRAY_MAPPING.put("char[]", CHAR_ARRAY_TYPE);
		PRIMITIVE_ARRAY_MAPPING.put("byte[]", BYTE_ARRAY_TYPE);
		PRIMITIVE_ARRAY_MAPPING.put("short[]", SHORT_ARRAY_TYPE);
		PRIMITIVE_ARRAY_MAPPING.put("int[]", INT_ARRAY_TYPE);
		PRIMITIVE_ARRAY_MAPPING.put("long[]", LONG_ARRAY_TYPE);
		PRIMITIVE_ARRAY_MAPPING.put("float[]", FLOAT_ARRAY_TYPE);
		PRIMITIVE_ARRAY_MAPPING.put("double[]", DOUBLE_ARRAY_TYPE);

		NativeLoader loader = SpringUtils.getBean(NativeLoader.class);
		if (loader != null) {
			VOID_WRAPPED_TYPE = loader.loadType("VOID_WRAPPED_TYPE");
			BOOLEAN_WRAPPED_TYPE = loader.loadType("BOOLEAN_WRAPPED_TYPE");
			CHAR_WRAPPED_TYPE = loader.loadType("CHAR_WRAPPED_TYPE");
			BYTE_WRAPPED_TYPE = loader.loadType("BYTE_WRAPPED_TYPE");
			SHORT_WRAPPED_TYPE = loader.loadType("SHORT_WRAPPED_TYPE");
			INT_WRAPPED_TYPE = loader.loadType("INT_WRAPPED_TYPE");
			LONG_WRAPPED_TYPE = loader.loadType("LONG_WRAPPED_TYPE");
			FLOAT_WRAPPED_TYPE = loader.loadType("FLOAT_WRAPPED_TYPE");
			DOUBLE_WRAPPED_TYPE = loader.loadType("DOUBLE_WRAPPED_TYPE");
			OBJECT_TYPE = loader.loadType("OBJECT_TYPE");
			STRING_TYPE = loader.loadType("STRING_TYPE");
			OBJECT_ARRAY_TYPE = loader.loadType("OBJECT_ARRAY_TYPE");
			STRING_ARRAY_TYPE = loader.loadType("STRING_ARRAY_TYPE");
			CLASS_TYPE = loader.loadType("CLASS_TYPE");
			LIST_TYPE = loader.loadType("LIST_TYPE");
			MAP_TYPE = loader.loadType("MAP_TYPE");
			NULL_TYPE = loader.loadType("NULL_TYPE");
			WILDCARD_TYPE = loader.loadType("WILDCARD_TYPE");
		} else {
			throw new RuntimeException("Basic types must be provided!");
		}
	}

	public static boolean isPrimitive(String className) {
		return SemanticParser.isPrimitive(className);
	}

	public static String getPrimitiveArrayTargetName(String className) {
		IType type = PRIMITIVE_ARRAY_TARGET_MAPPING.get(className);
		if (type != null)
			return type.getClassName();
		return null;
	}

	public static String getClassName(String simpleName) {

		String className = isPrimitive(simpleName) ? simpleName : null;
		if (StringUtils.isNotEmpty(className))
			return className;

		IType type = PRIMITIVE_ARRAY_MAPPING.get(simpleName);
		if (type != null)
			return type.getClassName();

		NativeLoader loader = SpringUtils.getBean(NativeLoader.class);
		if (loader != null)
			className = loader.findCommonType(simpleName);

		return StringUtils.isNotEmpty(className) ? className : null;
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

}
