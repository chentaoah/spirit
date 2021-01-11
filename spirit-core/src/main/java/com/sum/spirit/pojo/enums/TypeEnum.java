package com.sum.spirit.pojo.enums;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.sum.spirit.api.TypeEnumCtor;
import com.sum.spirit.core.build.AbstractSemanticParser;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeBuilder;

import cn.hutool.core.lang.Assert;

public enum TypeEnum {

	void_t, boolean_t, char_t, byte_t, short_t, int_t, long_t, float_t, double_t, //
	boolean_a, char_a, byte_a, short_a, int_a, long_a, float_a, double_a, //

	Void, Boolean, Character, Byte, Short, Integer, Long, Float, Double, //
	Object, String, Object_Array, String_Array, Class, List, Map, Null, Wildcard;

	public static final Map<String, IType> PRIMITIVE_ARRAY_TARGET_MAPPING = new ConcurrentHashMap<>();

	public static final Map<String, IType> PRIMITIVE_ARRAY_MAPPING = new ConcurrentHashMap<>();

	static {
		void_t.value = TypeBuilder.build("void", "void", "void", true/* primitive */, false, false, false, false);
		boolean_t.value = TypeBuilder.build("boolean", "boolean", "boolean", true/* primitive */, false, false, false, false);
		char_t.value = TypeBuilder.build("char", "char", "char", true/* primitive */, false, false, false, false);
		byte_t.value = TypeBuilder.build("byte", "byte", "byte", true/* primitive */, false, false, false, false);
		short_t.value = TypeBuilder.build("short", "short", "short", true/* primitive */, false, false, false, false);
		int_t.value = TypeBuilder.build("int", "int", "int", true/* primitive */, false, false, false, false);
		long_t.value = TypeBuilder.build("long", "long", "long", true/* primitive */, false, false, false, false);
		float_t.value = TypeBuilder.build("float", "float", "float", true/* primitive */, false, false, false, false);
		double_t.value = TypeBuilder.build("double", "double", "double", true/* primitive */, false, false, false, false);

		boolean_a.value = TypeBuilder.build("[Z", "boolean[]", "boolean[]", false, true/* array */, false, false, false);
		char_a.value = TypeBuilder.build("[C", "char[]", "char[]", false, true/* array */, false, false, false);
		byte_a.value = TypeBuilder.build("[B", "byte[]", "byte[]", false, true/* array */, false, false, false);
		short_a.value = TypeBuilder.build("[S", "short[]", "short[]", false, true/* array */, false, false, false);
		int_a.value = TypeBuilder.build("[I", "int[]", "int[]", false, true/* array */, false, false, false);
		long_a.value = TypeBuilder.build("[J", "long[]", "long[]", false, true/* array */, false, false, false);
		float_a.value = TypeBuilder.build("[F", "float[]", "float[]", false, true/* array */, false, false, false);
		double_a.value = TypeBuilder.build("[D", "double[]", "double[]", false, true/* array */, false, false, false);

		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[Z", boolean_t.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[C", char_t.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[B", byte_t.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[S", short_t.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[I", int_t.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[J", long_t.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[F", float_t.value);
		PRIMITIVE_ARRAY_TARGET_MAPPING.put("[D", double_t.value);

		PRIMITIVE_ARRAY_MAPPING.put("boolean[]", boolean_a.value);
		PRIMITIVE_ARRAY_MAPPING.put("char[]", char_a.value);
		PRIMITIVE_ARRAY_MAPPING.put("byte[]", byte_a.value);
		PRIMITIVE_ARRAY_MAPPING.put("short[]", short_a.value);
		PRIMITIVE_ARRAY_MAPPING.put("int[]", int_a.value);
		PRIMITIVE_ARRAY_MAPPING.put("long[]", long_a.value);
		PRIMITIVE_ARRAY_MAPPING.put("float[]", float_a.value);
		PRIMITIVE_ARRAY_MAPPING.put("double[]", double_a.value);

		List<TypeEnumCtor> ctors = SpringUtils.getBeansAndSort(TypeEnumCtor.class);
		Assert.notNull(ctors.size() == 0, "Type enum ctor must be provided!");
		for (TypeEnumCtor ctor : ctors) {
			ctor.prepareEnv();
		}
	}

	public static boolean isPrimitive(String className) {
		return AbstractSemanticParser.isPrimitive(className);
	}

	public static String getPrimitiveArrayTargetName(String className) {
		IType type = PRIMITIVE_ARRAY_TARGET_MAPPING.get(className);
		if (type != null) {
			return type.getClassName();
		}
		return null;
	}

	public static String getClassName(String simpleName) {
		String className = isPrimitive(simpleName) ? simpleName : null;
		if (StringUtils.isNotEmpty(className)) {
			return className;
		}

		IType type = PRIMITIVE_ARRAY_MAPPING.get(simpleName);
		if (type != null) {
			return type.getClassName();
		}

		return null;
	}

	public static IType getWrappedType(String className) {
		if (void_t.value.getClassName().equals(className)) {
			return Void.value;

		} else if (boolean_t.value.getClassName().equals(className)) {
			return Boolean.value;

		} else if (char_t.value.getClassName().equals(className)) {
			return Character.value;

		} else if (byte_t.value.getClassName().equals(className)) {
			return Byte.value;

		} else if (short_t.value.getClassName().equals(className)) {
			return Short.value;

		} else if (int_t.value.getClassName().equals(className)) {
			return Integer.value;

		} else if (long_t.value.getClassName().equals(className)) {
			return Long.value;

		} else if (float_t.value.getClassName().equals(className)) {
			return Float.value;

		} else if (double_t.value.getClassName().equals(className)) {
			return Double.value;
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
