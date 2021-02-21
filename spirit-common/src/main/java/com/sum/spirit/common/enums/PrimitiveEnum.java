package com.sum.spirit.common.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.lang.Assert;

public enum PrimitiveEnum {

	VOID("void", "void", "void", true/* primitive */, false), //
	BOOLEAN("boolean", "boolean", "boolean", true/* primitive */, false), //
	CHAR("char", "char", "char", true/* primitive */, false), //
	BYTE("byte", "byte", "byte", true/* primitive */, false), //
	SHORT("short", "short", "short", true/* primitive */, false), //
	INT("int", "int", "int", true/* primitive */, false), //
	LONG("long", "long", "long", true/* primitive */, false), //
	FLOAT("float", "float", "float", true/* primitive */, false), //
	DOUBLE("double", "double", "double", true/* primitive */, false), //

	BOOLEAN_ARRAY("[Z", "boolean[]", "boolean[]", false, true/* array */), //
	CHAR_ARRAY("[C", "char[]", "char[]", false, true/* array */), //
	BYTE_ARRAY("[B", "byte[]", "byte[]", false, true/* array */), //
	SHORT_ARRAY("[S", "short[]", "short[]", false, true/* array */), //
	INT_ARRAY("[I", "int[]", "int[]", false, true/* array */), //
	LONG_ARRAY("[J", "long[]", "long[]", false, true/* array */), //
	FLOAT_ARRAY("[F", "float[]", "float[]", false, true/* array */), //
	DOUBLE_ARRAY("[D", "double[]", "double[]", false, true/* array */); //

	public static final String PRIMITIVE_ENUM = "void|boolean|char|byte|short|int|long|float|double";

	public static final Map<String, PrimitiveEnum> CLASS_NAME_MAPPING = new ConcurrentHashMap<>();
	public static final Map<String, PrimitiveEnum> SIMPLE_NAME_MAPPING = new ConcurrentHashMap<>();

	static {
		for (PrimitiveEnum primitiveEnum : values()) {
			CLASS_NAME_MAPPING.put(primitiveEnum.className, primitiveEnum);
			SIMPLE_NAME_MAPPING.put(primitiveEnum.simpleName, primitiveEnum);
		}
	}

	public static boolean isPrimitive(String className) {
		return CLASS_NAME_MAPPING.containsKey(className) && CLASS_NAME_MAPPING.get(className).isPrimitive;
	}

	public static boolean isPrimitiveArray(String className) {
		return CLASS_NAME_MAPPING.containsKey(className) && CLASS_NAME_MAPPING.get(className).isArray;
	}

	public static boolean isPrimitiveBySimple(String simpleName) {
		return SIMPLE_NAME_MAPPING.containsKey(simpleName) && SIMPLE_NAME_MAPPING.get(simpleName).isPrimitive;
	}

	public static boolean isPrimitiveArrayBySimple(String simpleName) {
		return SIMPLE_NAME_MAPPING.containsKey(simpleName) && SIMPLE_NAME_MAPPING.get(simpleName).isArray;
	}

	public static String getPrimitiveArrayTargetName(String className) {
		Assert.isTrue(isPrimitiveArray(className), "Class name must be a primitive array!");
		String simpleName = CLASS_NAME_MAPPING.get(className).simpleName;
		return simpleName.substring(0, simpleName.length() - 2);
	}

	public static String tryGetClassName(String simpleName) {
		return SIMPLE_NAME_MAPPING.containsKey(simpleName) ? SIMPLE_NAME_MAPPING.get(simpleName).className : null;
	}

	public String className;
	public String simpleName;
	public String typeName;
	public boolean isPrimitive;
	public boolean isArray;

	private PrimitiveEnum(String className, String simpleName, String typeName, boolean isPrimitive, boolean isArray) {
		this.className = className;
		this.simpleName = simpleName;
		this.typeName = typeName;
		this.isPrimitive = isPrimitive;
		this.isArray = isArray;
	}

}
