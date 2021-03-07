package com.sum.spirit.java.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectUtils {

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("The class was not found!className:[" + className + "]");
		}
	}

	public static String getClassName(String targetName, boolean isArray) {
		try {
			Class<?> clazz = ReflectUtils.getClass("java.lang." + targetName);
			if (clazz != null) {
				return isArray ? "[L" + clazz.getName() + ";" : clazz.getName();
			}
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	public static boolean isIndefinite(Method method) {
		Parameter[] parameters = method.getParameters();
		if (parameters != null && parameters.length > 0) {
			Parameter lastParameter = parameters[parameters.length - 1];
			return lastParameter.toString().contains("...");
		}
		return false;
	}

	public static boolean isIndefinite(Parameter lastParameter) {
		return lastParameter.toString().contains("...");
	}

	public static boolean isAccessible(Member member, int... modifiers) {
		int mod = member.getModifiers();
		for (int modifier : modifiers) {
			if ((mod & modifier) != 0) {
				return true;
			}
		}
		return false;
	}

}
