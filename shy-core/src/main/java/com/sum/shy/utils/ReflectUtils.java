package com.sum.shy.utils;

import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

public class ReflectUtils {

	public static Class<?> getClass(String className) {
		try {
			Class<?> clazz = getBasicClass(className);// 基本类型
			return clazz != null ? clazz : Class.forName(className);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("The class was not found!className:[" + className + "]");
		}
	}

	public static Class<?> getBasicClass(String className) {
		switch (className) {
		case "void":
			return void.class;
		case "boolean":
			return boolean.class;
		case "char":
			return char.class;
		case "short":
			return short.class;
		case "int":
			return int.class;
		case "long":
			return long.class;
		case "float":
			return float.class;
		case "double":
			return double.class;
		case "byte":
			return byte.class;
		}
		return null;
	}

	public static String getClassBySimpleName(String simpleName) {

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
		// 未知类型
		case "?":
			return WildcardType.class.getName();

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
