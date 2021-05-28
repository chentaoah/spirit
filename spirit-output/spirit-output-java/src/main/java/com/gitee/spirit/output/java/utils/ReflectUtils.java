package com.gitee.spirit.output.java.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ReflectUtils {

	@SuppressWarnings("deprecation")
	public static ClassLoader getClassLoader(List<String> classpaths) {
		try {
			URL urls[] = new URL[classpaths.size()];
			for (int i = 0; i < classpaths.size(); ++i) {
				urls[i] = new File(classpaths.get(i)).toURL();
			}
			return new URLClassLoader(urls, ReflectUtils.class.getClassLoader());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

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
