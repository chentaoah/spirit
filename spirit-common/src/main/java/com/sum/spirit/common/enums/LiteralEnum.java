package com.sum.spirit.common.enums;

import java.util.regex.Pattern;

public enum LiteralEnum {
	;
	public static final Pattern NULL_PATTERN = Pattern.compile("^null$");
	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern CHAR_PATTERN = Pattern.compile("^'[\\s\\S]*'$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern LONG_PATTERN = Pattern.compile("^\\d+L$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STRING_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern LIST_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");

	public static final Pattern VISIT_INDEX_PATTERN = Pattern.compile("^\\[\\d+\\]$");

	public static boolean isNull(String word) {
		return NULL_PATTERN.matcher(word).matches();
	}

	public static boolean isBoolean(String word) {
		return BOOLEAN_PATTERN.matcher(word).matches();
	}

	public static boolean isChar(String word) {
		return CHAR_PATTERN.matcher(word).matches();
	}

	public static boolean isInt(String word) {
		return INT_PATTERN.matcher(word).matches();
	}

	public static boolean isLong(String word) {
		return LONG_PATTERN.matcher(word).matches();
	}

	public static boolean isDouble(String word) {
		return DOUBLE_PATTERN.matcher(word).matches();
	}

	public static boolean isString(String word) {
		return STRING_PATTERN.matcher(word).matches();
	}

	public static boolean isList(String word) {
		return !VISIT_INDEX_PATTERN.matcher(word).matches() && LiteralEnum.LIST_PATTERN.matcher(word).matches();
	}

	public static boolean isMap(String word) {
		return MAP_PATTERN.matcher(word).matches();
	}

}
