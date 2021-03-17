package com.sum.spirit.common.enums;

import java.util.regex.Pattern;

public class TypeEnum {
	;
	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*$");
	public static final Pattern TYPE_ARRAY_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]$");
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*<[\\s\\S]+>$");
	public static final Pattern CONST_VAR_PATTERN = Pattern.compile("^[A-Z_]{2,}$");
	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	public static boolean isTypePrefix(String word) {
		return PrimitiveEnum.isPrimitiveBySimple(word) || (!CONST_VAR_PATTERN.matcher(word).matches() && TYPE_PATTERN.matcher(word).matches());
	}

	public static boolean isType(String word) {
		return !CONST_VAR_PATTERN.matcher(word).matches()
				&& (PrimitiveEnum.isPrimitiveBySimple(word) || PrimitiveEnum.isPrimitiveArrayBySimple(word) || TypeEnum.TYPE_PATTERN.matcher(word).matches()
						|| TypeEnum.TYPE_ARRAY_PATTERN.matcher(word).matches() || TypeEnum.GENERIC_TYPE_PATTERN.matcher(word).matches());
	}

	public static boolean isTypeEnd(String word) {
		return TYPE_END_PATTERN.matcher(word).matches();
	}

}
