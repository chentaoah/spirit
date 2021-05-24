package com.sum.spirit.common.pattern;

import java.util.regex.Pattern;

public class CommonPattern {

	private static final Pattern PATH_PATTERN = Pattern.compile("^(\\w+\\.)+\\w+$");
	private static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");
	private static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	private static final Pattern VARIABLE_PATTERN = Pattern.compile("^[a-z]+\\w*$");
	private static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

	public static boolean isPath(String word) {
		return PATH_PATTERN.matcher(word).matches();
	}

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
