package com.sum.shy.core.utils;

public class ReflectUtils {

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("The class was not found!className:[" + className + "]");
		}
	}

	public static String getWrapType(String className) {
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
