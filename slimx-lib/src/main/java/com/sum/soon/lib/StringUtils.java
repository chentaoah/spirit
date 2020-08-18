package com.sum.soon.lib;

public class StringUtils {

	public static boolean isNotEmpty(String str) {
		return str != null && str.length() > 0;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean equals(String str, Object obj) {
		return str == obj || (str != null && str.equals(obj));
	}

}
