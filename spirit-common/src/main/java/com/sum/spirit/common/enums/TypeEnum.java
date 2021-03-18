package com.sum.spirit.common.enums;

import java.util.regex.Pattern;

public enum TypeEnum {
	;
	public static final Pattern CONST_VAR_PATTERN = Pattern.compile("^[A-Z_]{2,}$");
	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+\\.[A-Z]+\\w+$");

	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*$");
	public static final Pattern TYPE_ARRAY_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]$");
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*<[\\s\\S]+>$");

	public static final Pattern PRIMITIVE_ARRAY_SIZE_INIT_PATTERN = Pattern.compile("^(" + PrimitiveEnum.PRIMITIVE_ENUM + ")\\[\\d+\\]$");
	public static final Pattern PRIMITIVE_ARRAY_LITERAL_INIT_PATTERN = Pattern.compile("^(" + PrimitiveEnum.PRIMITIVE_ENUM + ")\\[\\]\\{[\\s\\S]*\\}$");
	public static final Pattern TYPE_ARRAY_SIZE_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\d+\\]$");
	public static final Pattern TYPE_ARRAY_LITERAL_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]\\{[\\s\\S]*\\}$");
	public static final Pattern TYPE_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*(<[\\s\\S]+>)?\\([\\s\\S]*\\)$");

	public static boolean isTypePrefix(String word) {
		return PrimitiveEnum.isPrimitiveBySimple(word) || isType(word);
	}

	public static boolean isTypeEnd(String word) {
		return TYPE_END_PATTERN.matcher(word).matches();
	}

	public static boolean isType(String word) {
		return !CONST_VAR_PATTERN.matcher(word).matches() && TYPE_PATTERN.matcher(word).matches();
	}

	public static boolean isTypeArray(String word) {
		return TYPE_ARRAY_PATTERN.matcher(word).matches();
	}

	public static boolean isGenericType(String word) {
		return GENERIC_TYPE_PATTERN.matcher(word).matches();
	}

	public static boolean isPrimitiveArraySizeInit(String word) {
		return PRIMITIVE_ARRAY_SIZE_INIT_PATTERN.matcher(word).matches();
	}

	public static boolean isPrimitiveArrayLiteralInit(String word) {
		return PRIMITIVE_ARRAY_LITERAL_INIT_PATTERN.matcher(word).matches();
	}

	public static boolean isTypeArraySizeInit(String word) {
		return TYPE_ARRAY_SIZE_INIT_PATTERN.matcher(word).matches();
	}

	public static boolean isTypeArrayLiteralInit(String word) {
		return TYPE_ARRAY_LITERAL_INIT_PATTERN.matcher(word).matches();
	}

	public static boolean isTypeInit(String word) {
		return TYPE_INIT_PATTERN.matcher(word).matches();
	}

	public static boolean isAnyType(String word) {
		return !CONST_VAR_PATTERN.matcher(word).matches() && //
				(PrimitiveEnum.isPrimitiveBySimple(word) || PrimitiveEnum.isPrimitiveArrayBySimple(word) || //
						isType(word) || isTypeArray(word) || isGenericType(word));
	}

	public static boolean isAnyInit(String word) {
		return isPrimitiveArraySizeInit(word) || isPrimitiveArrayLiteralInit(word) || //
				isTypeArraySizeInit(word) || isTypeArrayLiteralInit(word) || //
				isTypeInit(word);
	}

}
