package com.gitee.spirit.common.pattern;

import java.util.regex.Pattern;

import com.gitee.spirit.common.enums.TokenTypeEnum;

public class LiteralPattern {

	public static final Pattern NULL_PATTERN = Pattern.compile("^null$");
	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern CHAR_PATTERN = Pattern.compile("^'[\\s\\S]*'$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern LONG_PATTERN = Pattern.compile("^\\d+L$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STRING_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern LIST_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");
	// 常量也被认为是字面值的一种
	public static final Pattern CONST_VARIABLE_PATTERN = Pattern.compile("^[A-Z_]{2,}$");

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
		return !AccessPattern.isVisitIndex(word) && LiteralPattern.LIST_PATTERN.matcher(word).matches();
	}

	public static boolean isMap(String word) {
		return MAP_PATTERN.matcher(word).matches();
	}

	public static boolean isConstVariable(String word) {
		return CONST_VARIABLE_PATTERN.matcher(word).matches();
	}

	public static TokenTypeEnum getTokenType(String word) {
		if (isNull(word)) {
			return TokenTypeEnum.NULL;

		} else if (isBoolean(word)) {
			return TokenTypeEnum.BOOLEAN;

		} else if (isChar(word)) {
			return TokenTypeEnum.CHAR;

		} else if (isInt(word)) {
			return TokenTypeEnum.INT;

		} else if (isLong(word)) {
			return TokenTypeEnum.LONG;

		} else if (isDouble(word)) {
			return TokenTypeEnum.DOUBLE;

		} else if (isString(word)) {
			return TokenTypeEnum.STRING;

		} else if (isList(word)) {
			return TokenTypeEnum.LIST;

		} else if (isMap(word)) {
			return TokenTypeEnum.MAP;
		}
		return null;
	}

}
