package com.sum.spirit.common.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SymbolEnum {

	INCREASE("++", SymbolTypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 40, OperandEnum.LEFT),
	DECREASE("--", SymbolTypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 40, OperandEnum.LEFT),
	NEGATE("!", SymbolTypeEnum.OPERATOR, OperatorEnum.LOGICAL, 40, OperandEnum.RIGHT),
	MULTIPLY("*", SymbolTypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	DIVIDE("/", SymbolTypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	REMIND("%", SymbolTypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	ADD("+", SymbolTypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 30, OperandEnum.BINARY),
	SUBTRACT("-", SymbolTypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 30, OperandEnum.MULTIPLE),
	LEFT_SHIFT("<<", SymbolTypeEnum.OPERATOR, OperatorEnum.BITWISE, 25, OperandEnum.BINARY),
	RIGHT_SHIFT(">>", SymbolTypeEnum.OPERATOR, OperatorEnum.BITWISE, 25, OperandEnum.BINARY),
	BITWISE_NOT("~", SymbolTypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.RIGHT),
	BITWISE_AND("&", SymbolTypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.BINARY),
	BITWISE_OR("|", SymbolTypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.BINARY),
	BITWISE_XOR("^", SymbolTypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.BINARY),
	EQUAL("==", SymbolTypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	UNEQUAL("!=", SymbolTypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	LESS_EQUAL("<=", SymbolTypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	MORE_EQUAL(">=", SymbolTypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	LESS("<", SymbolTypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	MORE(">", SymbolTypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	AND("&&", SymbolTypeEnum.OPERATOR, OperatorEnum.LOGICAL, 10, OperandEnum.BINARY),
	OR("||", SymbolTypeEnum.OPERATOR, OperatorEnum.LOGICAL, 10, OperandEnum.BINARY),
	QUESTION_MARK("?", SymbolTypeEnum.OPERATOR, OperatorEnum.CONDITIONAL, 5, OperandEnum.BINARY),
	ASSIGN("=", SymbolTypeEnum.OPERATOR, OperatorEnum.ASSIGN, 5, OperandEnum.BINARY),

	LEFT_PARENTHESIS("(", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	RIGHT_PARENTHESIS(")", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	LEFT_ANGLE_BRACKET("<", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	RIGHT_ANGLE_BRACKET(">", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	LEFT_SQUARE_BRACKET("[", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	RIGHT_SQUARE_BRACKET("]", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	LEFT_CURLY_BRACKET("{", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	RIGHT_CURLY_BRACKET("}", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	COLON(":", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	DOUBLE_COLON("::", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	COMMA(",", SymbolTypeEnum.SEPARATOR, null, 0, null), //
	SEMICOLON(";", SymbolTypeEnum.SEPARATOR, null, 0, null); //

	public static final char[] SYMBOL_CHARS = new char[] { '+', '-', '!', '*', '/', '%', '<', '>', '~', '&', '|', '^', '=', '?', //
			'(', ')', '[', ']', '{', '}', ':', ',', ';' };

	public static final Map<String, SymbolEnum> SINGLE_SYMBOL = new ConcurrentHashMap<>();

	public static final Map<String, SymbolEnum> DOUBLE_SYMBOL = new ConcurrentHashMap<>();

	public static final Map<String, SymbolEnum> OPERATOR_MAP = new ConcurrentHashMap<>();

	public static final Map<String, SymbolEnum> SEPARATOR_MAP = new ConcurrentHashMap<>();

	static {
		for (SymbolEnum symbolEnum : values()) {
			if (symbolEnum.value.length() == 1) {
				SINGLE_SYMBOL.put(symbolEnum.value, symbolEnum);
			} else if (symbolEnum.value.length() == 2) {
				DOUBLE_SYMBOL.put(symbolEnum.value, symbolEnum);
			}
			if (symbolEnum.type == SymbolTypeEnum.OPERATOR) {
				OPERATOR_MAP.put(symbolEnum.value, symbolEnum);
			} else if (symbolEnum.type == SymbolTypeEnum.SEPARATOR) {
				SEPARATOR_MAP.put(symbolEnum.value, symbolEnum);
			}
		}
	}

	public static boolean isSymbolChar(char c) {
		for (char ch : SYMBOL_CHARS) {
			if (ch == c) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSingleSymbol(String value) {
		return SINGLE_SYMBOL.containsKey(value);
	}

	public static boolean isDoubleSymbol(String value) {
		return DOUBLE_SYMBOL.containsKey(value);
	}

	public static boolean isSymbol(String value) {
		return OPERATOR_MAP.containsKey(value) || SEPARATOR_MAP.containsKey(value);
	}

	public static boolean isOperator(String value) {
		return OPERATOR_MAP.containsKey(value);
	}

	public static boolean isSeparator(String value) {
		return SEPARATOR_MAP.containsKey(value);
	}

	public static SymbolEnum getOperator(String value) {
		return OPERATOR_MAP.get(value);
	}

	public static SymbolEnum getSeparator(String value) {
		return SEPARATOR_MAP.get(value);
	}

	public static int getPriority(String value) {
		return isOperator(value) ? getOperator(value).priority : -1;
	}

	public static boolean isArithmetic(String value) {
		return isOperator(value) && getOperator(value).category == OperatorEnum.ARITHMETIC;
	}

	public static boolean isBitwise(String value) {
		return isOperator(value) && getOperator(value).category == OperatorEnum.BITWISE;
	}

	public static boolean isRelation(String value) {
		return isOperator(value) && getOperator(value).category == OperatorEnum.RELATION;
	}

	public static boolean isLogical(String value) {
		return isOperator(value) && getOperator(value).category == OperatorEnum.LOGICAL;
	}

	public static boolean isConditional(String value) {
		return isOperator(value) && getOperator(value).category == OperatorEnum.CONDITIONAL;
	}

	public static boolean isAssign(String value) {
		return isOperator(value) && getOperator(value).category == OperatorEnum.ASSIGN;
	}

	// 符号值
	public String value;
	// 类型,操作符,还是分隔符
	public SymbolTypeEnum type;
	// 类别:算术运算符,位运算符,关系运算符,逻辑运算,条件运算符,赋值运算符
	public OperatorEnum category;
	// 优先级
	public int priority;
	// 操作数:左元,右元,二元,多义
	public OperandEnum operand;

	private SymbolEnum(String value, SymbolTypeEnum type, OperatorEnum category, int priority, OperandEnum operand) {
		this.value = value;
		this.type = type;
		this.category = category;
		this.priority = priority;
		this.operand = operand;
	}

	public enum SymbolTypeEnum {
		OPERATOR, // 操作符
		SEPARATOR // 分隔符
	}

	public enum OperatorEnum {
		ARITHMETIC, // 计算
		BITWISE, // 移位
		RELATION, // 关系
		LOGICAL, // 逻辑
		CONDITIONAL, // 条件
		ASSIGN // 赋值
	}

	public enum OperandEnum {
		LEFT, // 左元
		RIGHT, // 右元
		BINARY, // 二元
		MULTIPLE // 多义
	}

}
