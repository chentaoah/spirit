package com.sum.shy.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

	public static boolean isPrimitive(String name) {// 基本类型数组不算基本类型 className or simpleName
		return PRIMITIVE_PATTERN.matcher(name).matches();
	}

	public static boolean isArray(String name) {// className or simpleName or typeName
		return name.startsWith("[") || name.endsWith("[]");
	}

	public static String getTargetName(String name) {// className or simpleName or typeName

		if (name.contains(".") && name.contains("$"))
			name = name.replaceAll("\\$", ".");// 替换内部类path中的$符号

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

	public static String getClassName(String simpleName) {// 只能支持基本类型和基本类型数组

		if (isPrimitive(simpleName))
			return simpleName;// 基本类型simpleName和className一致

		switch (simpleName) {
		case "boolean[]":
			return boolean[].class.getName();// 基本类型数组
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
		case "List":
			return List.class.getName();
		case "Map":
			return Map.class.getName();
		}

		// 尝试从java.lang.包下获取类
		try {
			Class<?> clazz = ReflectUtils.getClass("java.lang." + getTargetName(simpleName));
			if (clazz != null)
				return !isArray(simpleName) ? clazz.getName() : "[L" + clazz.getName() + ";";
		} catch (Exception e) {
			// ignore
		}

		return null;
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
		System.out.println(clazz.getSuperclass());
		System.out.println(void.class.isPrimitive());
		System.out.println(void.class.getName());
		System.out.println(int.class.getSuperclass());
		System.out.println(int.class.getInterfaces());
		List<String> list = new ArrayList<>();
		List<Object> list1 = new ArrayList<>();
		System.out.println(list1.getClass().isAssignableFrom(list.getClass()));
		System.out.println(Object.class.isAssignableFrom(int[].class));
		System.out.println(Object.class.isAssignableFrom(int.class));

	}

}
