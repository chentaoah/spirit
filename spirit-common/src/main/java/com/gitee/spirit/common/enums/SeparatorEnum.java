package com.gitee.spirit.common.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SeparatorEnum {

	LEFT_PARENTHESIS("("), //
	RIGHT_PARENTHESIS(")"), //
	LEFT_ANGLE_BRACKET("<"), //
	RIGHT_ANGLE_BRACKET(">"), //
	LEFT_SQUARE_BRACKET("["), //
	RIGHT_SQUARE_BRACKET("]"), //
	LEFT_CURLY_BRACKET("{"), //
	RIGHT_CURLY_BRACKET("}"), //
	COLON(":"), //
	DOUBLE_COLON("::"), //
	COMMA(","), //
	SEMICOLON(";"); //

	public static final Map<String, SeparatorEnum> SEPARATOR_MAP = new ConcurrentHashMap<>();

	static {
		for (SeparatorEnum separatorEnum : values()) {
			SEPARATOR_MAP.put(separatorEnum.value, separatorEnum);
		}
	}

	public static boolean isSeparator(String value) {
		return SEPARATOR_MAP.containsKey(value);
	}

	public static SeparatorEnum getSeparator(String value) {
		return SEPARATOR_MAP.get(value);
	}

	public String value;

	private SeparatorEnum(String value) {
		this.value = value;
	}

}
