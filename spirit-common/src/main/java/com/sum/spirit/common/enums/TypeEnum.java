package com.sum.spirit.common.enums;

import java.util.regex.Pattern;

public class TypeEnum {
	;
	public static final Pattern CONST_VAR_PATTERN = Pattern.compile("^[A-Z_]{2,}$");
	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*$");

	public static boolean isPureType(String prefix) {
		return PrimitiveEnum.isPrimitiveBySimple(prefix) || (!CONST_VAR_PATTERN.matcher(prefix).matches() && TYPE_PATTERN.matcher(prefix).matches());
	}
}
