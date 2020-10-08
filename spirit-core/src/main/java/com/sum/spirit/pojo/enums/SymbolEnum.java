package com.sum.spirit.pojo.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum SymbolEnum {
	INCREASE("++", "\\+\\+", "\\+ \\+", TypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 40, OperandEnum.MULTIPLE),
	DECREASE("--", "--", "- -", TypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 40, OperandEnum.MULTIPLE),
	NOT("!", "\\!", TypeEnum.OPERATOR, OperatorEnum.LOGICAL, 40, OperandEnum.RIGHT),
	MULTIPLY("*", "\\*", TypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	DIVIDE("/", "/", TypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	REMIND("%", "%", TypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	ADD("+", "\\+", TypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 30, OperandEnum.BINARY),
	SUBTRACT("-", "-", TypeEnum.OPERATOR, OperatorEnum.ARITHMETIC, 30, OperandEnum.MULTIPLE),
	LEFT_SHIFT("<<", "<<", "< <", TypeEnum.OPERATOR, OperatorEnum.BITWISE, 25, OperandEnum.BINARY),
	RIGHT_SHIFT(">>", ">>", "> >", TypeEnum.OPERATOR, OperatorEnum.BITWISE, 25, OperandEnum.BINARY),
	BITWISE_NOT("~", "~", TypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.RIGHT),
	BITWISE_AND("&", "&", TypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.BINARY),
	BITWISE_OR("|", "[|]{1}", TypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.BINARY),
	BITWISE_XOR("^", "\\^", TypeEnum.OPERATOR, OperatorEnum.BITWISE, 20, OperandEnum.BINARY),
	EQUAL("==", "==", "= =", TypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	UNEQUAL("!=", "!=", "! =", TypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	LESS_EQUAL("<=", "<=", "< =", TypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	MORE_EQUAL(">=", ">=", "> =", TypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	LESS("<", "<", TypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	MORE(">", ">", TypeEnum.OPERATOR, OperatorEnum.RELATION, 15, OperandEnum.BINARY),
	AND("&&", "&&", "& &", TypeEnum.OPERATOR, OperatorEnum.LOGICAL, 10, OperandEnum.BINARY),
	OR("||", "[|]{2}", "\\| \\|", TypeEnum.OPERATOR, OperatorEnum.LOGICAL, 10, OperandEnum.BINARY),
	QUESTION_MARK("?", "\\?", TypeEnum.OPERATOR, OperatorEnum.CONDITIONAL, 5, OperandEnum.BINARY),
	ASSIGN("=", "=", TypeEnum.OPERATOR, OperatorEnum.ASSIGN, 5, OperandEnum.BINARY),

	LEFT_PARENTHESIS("(", "\\(", TypeEnum.SEPARATOR, null, 0, null), RIGHT_PARENTHESIS(")", "\\)", TypeEnum.SEPARATOR, null, 0, null),
	LEFT_ANGLE_BRACKET("<", "<", TypeEnum.SEPARATOR, null, 0, null), RIGHT_ANGLE_BRACKET(">", ">", TypeEnum.SEPARATOR, null, 0, null),
	LEFT_SQUARE_BRACKET("[", "\\[", TypeEnum.SEPARATOR, null, 0, null), RIGHT_SQUARE_BRACKET("]", "\\]", TypeEnum.SEPARATOR, null, 0, null),
	LEFT_CURLY_BRACKET("{", "\\{", TypeEnum.SEPARATOR, null, 0, null), RIGHT_CURLY_BRACKET("}", "\\}", TypeEnum.SEPARATOR, null, 0, null),
	COLON(":", "\\:", TypeEnum.SEPARATOR, null, 0, null), DOUBLE_COLON("::", "[:]{2}", "\\: \\:", TypeEnum.SEPARATOR, null, 0, null),
	COMMA(",", ",", TypeEnum.SEPARATOR, null, 0, null), SEMICOLON(";", ";", TypeEnum.SEPARATOR, null, 0, null);

	public static final List<SymbolEnum> SIGLE_SYMBOLS = new ArrayList<>();

	public static final List<SymbolEnum> DOUBLE_SYMBOLS = new ArrayList<>();

	public static final Map<String, SymbolEnum> OPERATOR_MAP = new LinkedHashMap<>();

	public static final Map<String, SymbolEnum> SEPARATOR_MAP = new LinkedHashMap<>();

	static {
		for (SymbolEnum symbolEnum : values()) {
			if (symbolEnum.value.length() == 1) {
				SIGLE_SYMBOLS.add(symbolEnum);
			} else if (symbolEnum.value.length() == 2) {
				DOUBLE_SYMBOLS.add(symbolEnum);
			}
			if (symbolEnum.type == TypeEnum.OPERATOR) {
				OPERATOR_MAP.put(symbolEnum.value, symbolEnum);
			} else if (symbolEnum.type == TypeEnum.SEPARATOR) {
				SEPARATOR_MAP.put(symbolEnum.value, symbolEnum);
			}
		}
	}

	public static boolean isSymbol(String value) {
		return OPERATOR_MAP.containsKey(value) || SEPARATOR_MAP.containsKey(value);
	}

	public static SymbolEnum getOperator(String value) {
		return OPERATOR_MAP.get(value);
	}

	public static SymbolEnum getSeparator(String value) {
		return SEPARATOR_MAP.get(value);
	}

	public static int getPriority(String value) {
		return isSymbol(value) ? getOperator(value).priority : -1;
	}

	public static boolean isArithmetic(String value) {
		return isSymbol(value) && getOperator(value).category == OperatorEnum.ARITHMETIC;
	}

	public static boolean isBitwise(String value) {
		return isSymbol(value) && getOperator(value).category == OperatorEnum.BITWISE;
	}

	public static boolean isRelation(String value) {
		return isSymbol(value) && getOperator(value).category == OperatorEnum.RELATION;
	}

	public static boolean isLogical(String value) {
		return isSymbol(value) && getOperator(value).category == OperatorEnum.LOGICAL;
	}

	public static boolean isConditional(String value) {
		return isSymbol(value) && getOperator(value).category == OperatorEnum.CONDITIONAL;
	}

	public static boolean isAssign(String value) {
		return isSymbol(value) && getOperator(value).category == OperatorEnum.ASSIGN;
	}

	// 符号值
	public String value;
	// 正则表达式
	public String regex;
	// 需要纠正的书写方式
	public String badRegex;
	// 类型,操作符,还是分隔符
	public TypeEnum type;
	// 类别:算术运算符,位运算符,关系运算符,逻辑运算,条件运算符,赋值运算符
	public OperatorEnum category;
	// 优先级
	public int priority;
	// 操作数:左元,右元,二元,多义
	public OperandEnum operand;

	private SymbolEnum(String value, String regex, String badRegex, TypeEnum type, OperatorEnum category, int priority, OperandEnum operand) {
		this.value = value;
		this.regex = regex;
		this.badRegex = badRegex;
		this.type = type;
		this.category = category;
		this.priority = priority;
		this.operand = operand;
	}

	private SymbolEnum(String value, String regex, TypeEnum type, OperatorEnum category, int priority, OperandEnum operand) {
		this(value, regex, null, type, category, priority, operand);
	}

	public enum TypeEnum {
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
