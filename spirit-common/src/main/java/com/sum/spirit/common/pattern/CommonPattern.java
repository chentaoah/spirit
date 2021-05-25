package com.sum.spirit.common.pattern;

import java.util.regex.Pattern;

public class CommonPattern {

	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");
	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	public static final Pattern VARIABLE_PATTERN = Pattern.compile("^[a-z]+\\w*$");
	public static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

	public static boolean isAnnotation(String word) {
		return ANNOTATION_PATTERN.matcher(word).matches();
	}

	public static boolean isSubexpress(String word) {
		return SUBEXPRESS_PATTERN.matcher(word).matches();
	}

	public static boolean isVariable(String word) {
		return VARIABLE_PATTERN.matcher(word).matches();
	}

	public static boolean isPrefix(String word) {
		return PREFIX_PATTERN.matcher(word).matches();
	}

}
