package com.sum.shy.sentence;

import java.util.regex.Pattern;

//语素
public class Morpheme {

	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern INVOKE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.]+\\([\\s\\S]*\\)$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+$");
	public static final Pattern INIT_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\([\\s\\S]*\\)$");

	public static String getType(String str) {

		if (BOOLEAN_PATTERN.matcher(str).matches()) {
			return "boolean";
		} else if (INT_PATTERN.matcher(str).matches()) {
			return "int";
		} else if (DOUBLE_PATTERN.matcher(str).matches()) {
			return "double";
		} else if (STR_PATTERN.matcher(str).matches()) {
			return "str";
		} else if (INVOKE_PATTERN.matcher(str).matches()) {
			return getInvokeType(str);
		} else if (ARRAY_PATTERN.matcher(str).matches()) {
			return "array";
		} else if (MAP_PATTERN.matcher(str).matches()) {
			return "map";
		} else if (VAR_PATTERN.matcher(str).matches()) {// 变量
			return "var";
		}
		return null;

	}

	public static boolean isInitInvoke(String str) {
		return INIT_PATTERN.matcher(str).matches();
	}

	public static String getInitMethod(String str) {
		return str.substring(0, str.indexOf("("));
	}

	private static String getInvokeType(String str) {
		// 构造函数
		if (isInitInvoke(str)) {
			return getInitMethod(str);
		}
		return "var";
	}

}
