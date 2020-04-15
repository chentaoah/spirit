package com.sum.shy.lib;

public class Assert {

	public static void isTrue(boolean bool, String message) {
		if (!bool)
			throw new IllegalArgumentException(message);
	}

	public static void notNull(Object object, String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(String str, String message) {
		if (StringUtils.isEmpty(str))
			throw new IllegalArgumentException(message);
	}
}
