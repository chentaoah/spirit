package com.gitee.spirit.core.clazz.utils;

import com.gitee.spirit.common.enums.PrimitiveEnum;

import cn.hutool.core.lang.Assert;

public class TypeUtils {

	public static String getPackage(String className) {
		return className.substring(0, className.lastIndexOf("."));
	}

	public static boolean isSamePackage(String className1, String className2) {
		String packageStr1 = className1.substring(0, className1.lastIndexOf('.'));
		String packageStr2 = className2.substring(0, className2.lastIndexOf('.'));
		return packageStr1.equals(packageStr2);
	}

	public static boolean isArray(String name) {// className or simpleName or typeName
		return name.startsWith("[") || name.endsWith("[]");
	}

	public static String getTargetName(String name) {// className or simpleName or typeName
		// 泛型
		if (name.contains("<") && name.endsWith(">")) {
			return name.substring(0, name.indexOf('<'));
		}
		// 内部类
		if (name.contains(".") && name.contains("$")) {
			name = name.replaceAll("\\$", ".");
		}
		// 数组
		if (!isArray(name)) {
			return name;

		} else if (name.startsWith("[L") && name.endsWith(";")) {
			return name.substring(2, name.length() - 1);

		} else if (name.endsWith("[]")) {
			return name.replace("[]", "");

		} else if (name.startsWith("[")) {
			// [Z 转换成 boolean
			String targetName = PrimitiveEnum.getTargetName(name);
			Assert.notEmpty(targetName, "Target name cannot be empty!");
			return targetName;
		}

		throw new RuntimeException("Failed to get target name!");
	}

	public static String getArrayName(String className) {
		if (isArray(className)) {
			return className;
		}
		if (PrimitiveEnum.isPrimitive(className)) {
			return PrimitiveEnum.getArrayName(className);
		}
		return "[L" + className + ";";
	}

	public static String getClassName(boolean isArray, String className) {
		return isArray ? getArrayName(className) : className;
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

}
