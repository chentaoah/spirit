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
