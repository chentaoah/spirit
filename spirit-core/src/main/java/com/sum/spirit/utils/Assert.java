package com.sum.spirit.utils;

import org.apache.commons.lang3.StringUtils;

public class Assert {

	public static void isTrue(boolean bool, String message) {
		if (!bool) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notNull(Object obj, String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(String str, String message) {
		if (StringUtils.isEmpty(str)) {
			throw new IllegalArgumentException(message);
		}
	}
}
