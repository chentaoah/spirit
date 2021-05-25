package com.sum.spirit.common.pattern;

import java.util.regex.Pattern;

import com.sum.spirit.common.enums.PrimitiveEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;

public class TypePattern {

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

	public static boolean isType(String word) {
		return !LiteralPattern.isConstVariable(word) && TYPE_PATTERN.matcher(word).matches();
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
		return !LiteralPattern.isConstVariable(word) && (PrimitiveEnum.isPrimitiveBySimple(word) || PrimitiveEnum.isPrimitiveArrayBySimple(word) || isType(word)
				|| isTypeArray(word) || isGenericType(word));
	}

	public static TokenTypeEnum getTokenType(String word) {
		if (isPrimitiveArraySizeInit(word) || isPrimitiveArrayLiteralInit(word)) {
			return TokenTypeEnum.ARRAY_INIT;

		} else if (isTypeArraySizeInit(word) || isTypeArrayLiteralInit(word)) {
			return TokenTypeEnum.ARRAY_INIT;

		} else if (isTypeInit(word)) {
			return TokenTypeEnum.TYPE_INIT;
		}
		return null;
	}

}
