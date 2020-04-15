package com.sum.shy.core.utils;

import java.io.File;
import java.util.regex.Pattern;

import com.sum.shy.core.lexical.SemanticDelegate;

public class TypeUtils {

	public static final Pattern PRIMITIVE_PATTERN = Pattern.compile("^(" + SemanticDelegate.PRIMITIVE_ENUM + ")$");

	public static String getNameByFile(File file) {
		return file.getName().replace(".shy", "");
	}

	public static String getPackage(String className) {
		className = getTargetName(className);
		return className.substring(0, className.lastIndexOf("."));
	}

	public static boolean isPrimitive(String className) {// 基本类型数组不算基本类型
		return PRIMITIVE_PATTERN.matcher(className).matches();
	}

	public static boolean isArray(String name) {// className or simpleName or typeName
		return name.startsWith("[") || name.endsWith("[]");
	}

	public static String getTargetName(String name) {// className or simpleName or typeName

		if (!isArray(name)) {
			return name;

		} else if (name.startsWith("[") && !(name.startsWith("[L") && name.endsWith(";"))) {// 基本类型数组className
			Class<?> clazz = ReflectUtils.getClass(name);
			return clazz.getSimpleName().replace("[]", "");// [Z 转换成 boolean[]

		} else if (name.startsWith("[L") && name.endsWith(";")) {// 高级类型数组className
			return name.substring(2, name.length() - 1);

		} else if (name.endsWith("[]")) {
			return name.replace("[]", "");
		}

		throw new RuntimeException("Failed to get target name!");
	}

	public static String getLastName(String className) {
		className = getTargetName(className);
		return className.substring(className.lastIndexOf(".") + 1);
	}

	public static String getSimpleName(String className) {
		return getLastName(className) + (isArray(className) ? "[]" : "");
	}

	public static String getTypeName(String className) {
		return getTargetName(className) + (isArray(className) ? "[]" : "");
	}

	public static void main(String[] args) throws Exception {
		Class<?>[] classes = new Class[] { boolean.class, boolean[].class, char.class, char[].class, short.class,
				short[].class, int.class, int[].class, long.class, long[].class, float.class, float[].class,
				double.class, double[].class, byte.class, byte[].class, Object.class, Object[].class, String.class,
				String[].class };
		for (Class<?> clazz : classes) {
			System.out.println("=== " + clazz.getSimpleName() + ".class ===");
			System.out.println(clazz.getName());
			System.out.println(clazz.getSimpleName());
			System.out.println(clazz.getTypeName());
		}
		System.out.println("======");
		Class<?> clazz = Class.forName("[Z");
		System.out.println(clazz.getName());
		System.out.println(clazz.isPrimitive());
	}

}
