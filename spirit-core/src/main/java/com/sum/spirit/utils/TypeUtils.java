package com.sum.spirit.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.common.TypeTable;

public class TypeUtils {

	public static String getPackage(String className) {
		return className.substring(0, className.lastIndexOf("."));
	}

	public static List<String> splitName(String simpleName) {
		List<String> names = Splitter.on(CharMatcher.anyOf("<,>")).trimResults().splitToList(simpleName);
		return new ArrayList<>(names);
	}

	public static boolean isArray(String name) {// className or simpleName or typeName
		return name.startsWith("[") || name.endsWith("[]");
	}

	public static String getTargetName(String name) {// className or simpleName or typeName

		// 泛型
		if (name.contains("<") && name.endsWith(">"))
			return name.substring(0, name.indexOf('<'));

		// 内部类
		if (name.contains(".") && name.contains("$"))
			name = name.replaceAll("\\$", ".");

		// 数组
		if (!isArray(name)) {
			return name;

		} else if (name.startsWith("[L") && name.endsWith(";")) {
			return name.substring(2, name.length() - 1);

		} else if (name.endsWith("[]")) {
			return name.replace("[]", "");

		} else if (name.startsWith("[")) {
			String targetName = TypeTable.getTargetName(name);// [Z 转换成 boolean
			Assert.notEmpty(targetName, "Target name cannot be empty!");
			return targetName;
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

}