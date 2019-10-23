package com.sum.shy.utils;

import java.util.regex.Pattern;

public class UnitUtils {

	public static final Pattern INIT_PATTERN = Pattern.compile("^[A-Z]+[a-z0-9]*$");

	// 判断是否构造方法
	public static boolean isInitMethod(String unit) {
		return INIT_PATTERN.matcher(unit).matches();
	}

}
