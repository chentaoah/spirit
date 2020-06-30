package com.sum.shy.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectUtils {

	public static Class<?> getClass(String className) {
		try {
			Class<?> clazz = getPrimitive(className);// 基础类型
			if (clazz == null)
				clazz = Class.forName(className);// 基础类型数组和一般类型
			return clazz;

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("The class was not found!className:[" + className + "]");
		}
	}

	public static boolean isIndefinite(Method method) {
		Parameter[] parameters = method.getParameters();
		if (parameters != null && parameters.length > 0) {
			Parameter lastParameter = parameters[parameters.length - 1];
			return lastParameter.toString().contains("...");
		}
		return false;
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

	public static Class<?> getWrappedType(String className) {
		switch (className) {
		case "boolean":
			return Boolean.class;
		case "char":
			return Character.class;
		case "short":
			return Short.class;
		case "int":
			return Integer.class;
		case "long":
			return Long.class;
		case "float":
			return Float.class;
		case "double":
			return Double.class;
		case "byte":
			return Byte.class;
		default:
			return null;
		}
	}

	public static boolean isMatch(int mod, int... modifiers) {
		for (int modifier : modifiers) {
			if ((mod & modifier) != 0)
				return true;
		}
		return false;
	}

}
