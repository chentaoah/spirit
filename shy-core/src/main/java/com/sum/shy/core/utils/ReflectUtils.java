package com.sum.shy.core.utils;

import java.util.List;
import java.util.Map;

public class ReflectUtils {

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("The class was not found!className:[" + className + "]");
		}
	}

	public static boolean isArray(String simpleName) {
		return simpleName.endsWith("[]");
	}

	public static String getTypeName(String simpleName) {
		return isArray(simpleName) ? simpleName.substring(0, simpleName.indexOf("[")) : simpleName;
	}

	public static String getClassName(String className) {
		return className.startsWith("[L") && className.endsWith(";") ? className.substring(2, className.length() - 1)
				: className;
	}

	public static String getCommonType(String simpleName) {

		switch (simpleName) {
		// 空类型
		case "void":
			return void.class.getName();
		// 基本类型
		case "boolean":
			return boolean.class.getName();
		case "char":
			return char.class.getName();
		case "short":
			return short.class.getName();
		case "int":
			return int.class.getName();
		case "long":
			return long.class.getName();
		case "float":
			return float.class.getName();
		case "double":
			return double.class.getName();
		case "byte":
			return byte.class.getName();
		// 基本类型数组
		case "boolean[]":
			return boolean[].class.getName();
		case "char[]":
			return char[].class.getName();
		case "short[]":
			return short[].class.getName();
		case "int[]":
			return int[].class.getName();
		case "long[]":
			return long[].class.getName();
		case "float[]":
			return float[].class.getName();
		case "double[]":
			return double[].class.getName();
		case "byte[]":
			return byte[].class.getName();
		// 包装类
		case "Boolean":
			return Boolean.class.getName();
		case "Character":
			return Character.class.getName();
		case "Short":
			return Short.class.getName();
		case "Integer":
			return Integer.class.getName();
		case "Long":
			return Long.class.getName();
		case "Float":
			return Float.class.getName();
		case "Double":
			return Double.class.getName();
		case "Byte":
			return Byte.class.getName();
		// 包装类数组
		case "Boolean[]":
			return Boolean[].class.getName();
		case "Character[]":
			return Character[].class.getName();
		case "Short[]":
			return Short[].class.getName();
		case "Integer[]":
			return Integer[].class.getName();
		case "Long[]":
			return Long[].class.getName();
		case "Float[]":
			return Float[].class.getName();
		case "Double[]":
			return Double[].class.getName();
		case "Byte[]":
			return Byte[].class.getName();
		// 类
		case "Object":
			return Object.class.getName();
		case "String":
			return String.class.getName();
		case "Class":
			return Class.class.getName();
		case "Exception":
			return Exception.class.getName();
		// 类数组
		case "Object[]":
			return Object[].class.getName();
		case "String[]":
			return String[].class.getName();

		default:
			return null;
		}
	}

	public static String getCollectionType(String typeName) {
		switch (typeName) {
		case "List":
			return List.class.getName();
		case "Map":
			return Map.class.getName();
		default:
			return null;
		}

	}

	public static String getWrapType(String className) {
		switch (className) {
		case "boolean":
			return Boolean.class.getSimpleName();
		case "char":
			return Character.class.getSimpleName();
		case "short":
			return Short.class.getSimpleName();
		case "int":
			return Integer.class.getSimpleName();
		case "long":
			return Long.class.getSimpleName();
		case "float":
			return Float.class.getSimpleName();
		case "double":
			return Double.class.getSimpleName();
		case "byte":
			return Byte.class.getSimpleName();
		default:
			return null;
		}
	}

}
