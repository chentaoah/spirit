package com.sum.spirit.pojo.enums;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sum.spirit.api.ClassLoader;
import com.sum.spirit.core.lexer.AbsSemanticParser;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.utils.SpringUtils;

public enum TypeEnum {
	VOID, //
	BOOLEAN, //
	CHAR, //
	BYTE, //
	SHORT, //
	INT, //
	LONG, //
	FLOAT, //
	DOUBLE, //
	BOOLEAN_ARRAY, //
	CHAR_ARRAY, //
	BYTE_ARRAY, //
	SHORT_ARRAY, //
	INT_ARRAY, //
	LONG_ARRAY, //
	FLOAT_ARRAY, //
	DOUBLE_ARRAY, //
	VOID_WRAPPED, //
	BOOLEAN_WRAPPED, //
	CHAR_WRAPPED, //
	BYTE_WRAPPED, //
	SHORT_WRAPPED, //
	INT_WRAPPED, //
	LONG_WRAPPED, //
	FLOAT_WRAPPED, //
	DOUBLE_WRAPPED, //
	OBJECT, //
	STRING, //
	OBJECT_ARRAY, //
	STRING_ARRAY, //
	CLASS, //
	LIST, //
	MAP, //
	NULL, //
	WILDCARD;//

	public static final Map<String, IType> PRIMITIVE_ARRAY_TARGET_MAPPING = new ConcurrentHashMap<>();

	public static final Map<String, IType> PRIMITIVE_ARRAY_MAPPING = new ConcurrentHashMap<>();

	static {
		VOID.value = IType.build("void", "void", "void", true/* primitive */, false, false, false, false);
		BOOLEAN.value = IType.build("boolean", "boolean", "boolean", true/* primitive */, false, false, false, false);
		CHAR.value = IType.build("char", "char", "char", true/* primitive */, false, false, false, false);
		BYTE.value = IType.build("byte", "byte", "byte", true/* primitive */, false, false, false, false);
		SHORT.value = IType.build("short", "short", "short", true/* primitive */, false, false, false, false);
		INT.value = IType.build("int", "int", "int", true/* primitive */, false, false, false, false);
		LONG.value = IType.build("long", "long", "long", true/* primitive */, false, false, false, false);
		FLOAT.value = IType.build("float", "float", "float", true/* primitive */, false, false, false, false);
		DOUBLE.value = IType.build("double", "double", "double", true/* primitive */, false, false, false, false);

		BOOLEAN_ARRAY.value = IType.build("[Z", "boolean[]", "boolean[]", false, true/* array */, false, false, false);
		CHAR_ARRAY.value = IType.build("[C", "char[]", "char[]", false, true/* array */, false, false, false);
		BYTE_ARRAY.value = IType.build("[B", "byte[]", "byte[]", false, true/* array */, false, false, false);
		SHORT_ARRAY.value = IType.build("[S", "short[]", "short[]", false, true/* array */, false, false, false);
		INT_ARRAY.value = IType.build("[I", "int[]", "int[]", false, true/* array */, false, false, false);
		LONG_ARRAY.value = IType.build("[J", "long[]", "long[]", false, true/* array */, false, false, false);
		FLOAT_ARRAY.value = IType.build("[F", "float[]", "float[]", false, true/* array */, false, false, false);
		DOUBLE_ARRAY.value = IType.build("[D", "double[]", "double[]", false, true/* array */, false, false, false);

		// Get target type by array type
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[Z", BOOLEAN.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[C", CHAR.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[B", BYTE.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[S", SHORT.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[I", INT.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[J", LONG.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[F", FLOAT.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[D", DOUBLE.value);

		// Get primitive type by array type
		PRIMITIVE_ARRAY_MAPPING.put("boolean[]", BOOLEAN_ARRAY.value);
		PRIMITIVE_ARRAY_MAPPING.put("char[]", CHAR_ARRAY.value);
		PRIMITIVE_ARRAY_MAPPING.put("byte[]", BYTE_ARRAY.value);
		PRIMITIVE_ARRAY_MAPPING.put("short[]", SHORT_ARRAY.value);
		PRIMITIVE_ARRAY_MAPPING.put("int[]", INT_ARRAY.value);
		PRIMITIVE_ARRAY_MAPPING.put("long[]", LONG_ARRAY.value);
		PRIMITIVE_ARRAY_MAPPING.put("float[]", FLOAT_ARRAY.value);
		PRIMITIVE_ARRAY_MAPPING.put("double[]", DOUBLE_ARRAY.value);

		// 类加载器
		List<ClassLoader> classLoaders = SpringUtils.getBeansAndSort(ClassLoader.class);
		Assert.notNull(classLoaders.size() == 0, "Class loader must be provided!");
		for (ClassLoader classLoader : classLoaders)
			classLoader.load();
	}

	public static boolean isPrimitive(String className) {
		return AbsSemanticParser.isPrimitive(className);
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

		List<ClassLoader> classLoaders = SpringUtils.getBeansAndSort(ClassLoader.class);
		for (ClassLoader classLoader : classLoaders) {
			className = classLoader.getClassName(simpleName);
			if (StringUtils.isNotEmpty(className))
				return className;
		}

		return null;
	}

	public static IType getWrappedType(String className) {
		if (VOID.value.getClassName().equals(className)) {
			return VOID_WRAPPED.value;

		} else if (BOOLEAN.value.getClassName().equals(className)) {
			return BOOLEAN_WRAPPED.value;

		} else if (CHAR.value.getClassName().equals(className)) {
			return CHAR_WRAPPED.value;

		} else if (BYTE.value.getClassName().equals(className)) {
			return BYTE_WRAPPED.value;

		} else if (SHORT.value.getClassName().equals(className)) {
			return SHORT_WRAPPED.value;

		} else if (INT.value.getClassName().equals(className)) {
			return INT_WRAPPED.value;

		} else if (LONG.value.getClassName().equals(className)) {
			return LONG_WRAPPED.value;

		} else if (FLOAT.value.getClassName().equals(className)) {
			return FLOAT_WRAPPED.value;

		} else if (DOUBLE.value.getClassName().equals(className)) {
			return DOUBLE_WRAPPED.value;
		}
		return null;
	}

	public IType value;

	private TypeEnum() {
	}

	private TypeEnum(IType value) {
		this.value = value;
	}

}
