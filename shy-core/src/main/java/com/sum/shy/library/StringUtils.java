package com.sum.shy.library;

public class StringUtils {

	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static boolean equals(String string1, String string2) {
		return string1 != null && string1.equals(string2);
	}

}
