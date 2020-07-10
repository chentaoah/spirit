package com.sum.shy.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.lexer.SemanticParserImpl;
import com.sum.shy.lib.Assert;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.pojo.common.TypeTable;

public class TypeUtils {

	public static String getPackage(String className) {
		className = getTargetName(className);
		return className.substring(0, className.lastIndexOf("."));
	}

	public static List<String> splitName(String simpleName) {
		List<String> names = Splitter.on(CharMatcher.anyOf("<,>")).trimResults().splitToList(simpleName);
		return new ArrayList<>(names);
	}

	public static boolean isPrimitive(String className) {// 基本类型数组不算基本类型 className or simpleName
		return SemanticParserImpl.PRIMITIVE_PATTERN.matcher(className).matches();
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

	public static String getClassName(String simpleName) {

		// 只能支持基本类型和基本类型数组
		String className = TypeTable.getClassName(simpleName);
		if (StringUtils.isNotEmpty(className))
			return className;

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

}
