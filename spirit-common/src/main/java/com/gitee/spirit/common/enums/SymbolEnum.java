package com.gitee.spirit.common.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.util.ArrayUtil;

public enum SymbolEnum {
	;
	public static final Map<String, String> SINGLE_SYMBOL = new ConcurrentHashMap<>();
	public static final Map<String, String> DOUBLE_SYMBOL = new ConcurrentHashMap<>();
	public static final char[] SYMBOL_CHARS = new char[] { '+', '-', '!', '*', '/', '%', '<', '>', '~', '&', '|', '^', '=', '?', //
			'(', ')', '[', ']', '{', '}', ':', ',', ';' };

	static {
		for (OperatorEnum operatorEnum : OperatorEnum.values()) {
			if (operatorEnum.value.length() == 1) {
				SINGLE_SYMBOL.put(operatorEnum.value, operatorEnum.value);

			} else if (operatorEnum.value.length() == 2) {
				DOUBLE_SYMBOL.put(operatorEnum.value, operatorEnum.value);
			}
		}
		for (SeparatorEnum separatorEnum : SeparatorEnum.values()) {
			if (separatorEnum.value.length() == 1) {
				SINGLE_SYMBOL.put(separatorEnum.value, separatorEnum.value);

			} else if (separatorEnum.value.length() == 2) {
				DOUBLE_SYMBOL.put(separatorEnum.value, separatorEnum.value);
			}
		}
	}

	public static boolean isSymbolChar(char c) {
		return ArrayUtil.contains(SYMBOL_CHARS, c);
	}

	public static boolean isSingleSymbol(String value) {
		return SINGLE_SYMBOL.containsKey(value);
	}

	public static boolean isDoubleSymbol(String value) {
		return DOUBLE_SYMBOL.containsKey(value);
	}

	public static boolean isSymbol(String value) {
		return OperatorEnum.isOperator(value) || SeparatorEnum.isSeparator(value);
	}

}
