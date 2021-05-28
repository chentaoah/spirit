package com.gitee.spirit.common.pattern;

import java.util.regex.Pattern;

import com.gitee.spirit.common.enums.TokenTypeEnum;

public class AccessPattern {

	public static final Pattern ACCESS_PATH_PATTERN = Pattern.compile("^(\\w+\\.)+[A-Z]+\\w+(\\.[a-z]+\\w*)?$");
	public static final Pattern LOCAL_METHOD_PATTERN = Pattern.compile("^[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_FIELD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*$");
	public static final Pattern VISIT_METHOD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_INDEX_PATTERN = Pattern.compile("^\\[\\d+\\]$");

	public static boolean isAccessPath(String word) {
		return ACCESS_PATH_PATTERN.matcher(word).matches();
	}

	public static boolean isLocalMethod(String word) {
		return LOCAL_METHOD_PATTERN.matcher(word).matches();
	}

	public static boolean isVisitField(String word) {
		return VISIT_FIELD_PATTERN.matcher(word).matches();
	}

	public static boolean isVisitMethod(String word) {
		return VISIT_METHOD_PATTERN.matcher(word).matches();
	}

	public static boolean isVisitIndex(String word) {
		return VISIT_INDEX_PATTERN.matcher(word).matches();
	}

	public static TokenTypeEnum getTokenType(String word) {
		if (isLocalMethod(word)) {
			return TokenTypeEnum.LOCAL_METHOD;

		} else if (isVisitField(word)) {
			return TokenTypeEnum.VISIT_FIELD;

		} else if (isVisitMethod(word)) {
			return TokenTypeEnum.VISIT_METHOD;

		} else if (isVisitIndex(word)) {
			return TokenTypeEnum.VISIT_INDEX;
		}
		return null;
	}

}
