package com.sum.shy.core.utils;

import java.io.File;

public class TypeUtils {

	public static String getPackage(String className) {
		return className.substring(0, className.lastIndexOf("."));
	}

	public static boolean isArray(String simpleName) {
		return simpleName.endsWith("[]");
	}

	public static String getTypeName(String simpleName) {
		return isArray(simpleName) ? simpleName.substring(0, simpleName.indexOf("[")) : simpleName;
	}

	public static String getClassName(String className) {
		return className.startsWith("[L") && className.endsWith(";") ? className.substring(2, className.length() - 1)
				: className;
	}

	public static String getTypeNameByFile(File file) {
		return file.getName().replace(".shy", "");
	}
}
