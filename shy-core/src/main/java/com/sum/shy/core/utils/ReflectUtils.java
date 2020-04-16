package com.sum.shy.core.utils;

public class ReflectUtils {

	public static Class<?> getClass(String className) {
		try {
			Class<?> clazz = getPrimitive(className);
			if (clazz == null)
				clazz = Class.forName(className);
			return clazz;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("The class was not found!className:[" + className + "]");
		}
	}

	public static Class<?> getPrimitive(String className) {
		switch (className) {
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
		default:
			return null;
		}
	}

	public static String getWrapperType(String className) {
		switch (className) {
		case "boolean":
			return Boolean.class.getName();
		case "char":
			return Character.class.getName();
		case "short":
			return Short.class.getName();
		case "int":
			return Integer.class.getName();
		case "long":
			return Long.class.getName();
		case "float":
			return Float.class.getName();
		case "double":
			return Double.class.getName();
		case "byte":
			return Byte.class.getName();
		default:
			return null;
		}
	}

}
