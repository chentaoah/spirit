package com.sum.spirit.pojo.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum SymbolEnum {
	INCREASE("++", "\\+\\+", "\\+ \\+", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.ARITHMETIC, 40, OperandEnum.MULTIPLE),
	DECREASE("--", "--", "- -", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.ARITHMETIC, 40, OperandEnum.MULTIPLE),
	NOT("!", "\\!", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.LOGICAL, 40, OperandEnum.RIGHT),
	MULTIPLY("*", "\\*", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	DIVIDE("/", "/", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	REMIND("%", "%", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.ARITHMETIC, 35, OperandEnum.BINARY),
	ADD("+", "\\+", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.ARITHMETIC, 30, OperandEnum.BINARY),
	SUBTRACT("-", "-", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.ARITHMETIC, 30, OperandEnum.MULTIPLE),
	LEFT_SHIFT("<<", "<<", "< <", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.BITWISE, 25, OperandEnum.BINARY),
	RIGHT_SHIFT(">>", ">>", "> >", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.BITWISE, 25, OperandEnum.BINARY),
	BITWISE_NOT("~", "~", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.BITWISE, 20, OperandEnum.RIGHT),
	BITWISE_AND("&", "&", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.BITWISE, 20, OperandEnum.BINARY),
	BITWISE_OR("|", "[|]{1}", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.BITWISE, 20, OperandEnum.BINARY),
	BITWISE_XOR("^", "\\^", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.BITWISE, 20, OperandEnum.BINARY),
	EQUAL("==", "==", "= =", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.RELATION, 15, OperandEnum.BINARY),
	UNEQUAL("!=", "!=", "! =", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.RELATION, 15, OperandEnum.BINARY),
	LESS_EQUAL("<=", "<=", "< =", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.RELATION, 15, OperandEnum.BINARY),
	MORE_EQUAL(">=", ">=", "> =", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.RELATION, 15, OperandEnum.BINARY),
	LESS("<", "<", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.RELATION, 15, OperandEnum.BINARY),
	MORE(">", ">", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.RELATION, 15, OperandEnum.BINARY),
	AND("&&", "&&", "& &", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.LOGICAL, 10, OperandEnum.BINARY),
	OR("||", "[|]{2}", "\\| \\|", CharNumberEnum.DOUBLE, TypeEnum.OPERATOR, CategoryEnum.LOGICAL, 10, OperandEnum.BINARY),
	JUDGE("?", "\\?", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.CONDITIONAL, 5, OperandEnum.BINARY),
	ASSIGN("=", "=", CharNumberEnum.SINGLE, TypeEnum.OPERATOR, CategoryEnum.ASSIGN, 5, OperandEnum.BINARY),
	LEFT_PARENTHESIS("(", "\\(", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	RIGHT_PARENTHESIS(")", "\\)", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	LEFT_SQUARE_BRACKET("[", "\\[", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	RIGHT_SQUARE_BRACKET("]", "\\]", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	LEFT_CURLY_BRACKET("{", "\\{", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	RIGHT_CURLY_BRACKET("}", "\\}", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	COLON(":", "\\:", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	DOUBLE_COLON("::", "[:]{2}", "\\: \\:", CharNumberEnum.DOUBLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	COMMA(",", ",", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN),
	SEMICOLON(";", ";", CharNumberEnum.SINGLE, TypeEnum.SEPARATOR, CategoryEnum.UNKNOWN, 0, OperandEnum.UNKNOWN);

	public static final List<SymbolEnum> SIGLE_SYMBOLS = new ArrayList<>();

	public static final List<SymbolEnum> DOUBLE_SYMBOLS = new ArrayList<>();

	public static final Map<String, SymbolEnum> SYMBOL_MAP = new LinkedHashMap<>();

	static {
		for (SymbolEnum symbolEnum : values()) {
			if (symbolEnum.charNumber == CharNumberEnum.SINGLE) {
				SIGLE_SYMBOLS.add(symbolEnum);
			} else if (symbolEnum.charNumber == CharNumberEnum.DOUBLE) {
				DOUBLE_SYMBOLS.add(symbolEnum);
			}
			SYMBOL_MAP.put(symbolEnum.value, symbolEnum);
		}
	}

	public static boolean isSymbol(String value) {
		return SYMBOL_MAP.containsKey(value);
	}

	public static SymbolEnum getSymbol(String value) {
		return SYMBOL_MAP.get(value);
	}

	public static int getPriority(String value) {
		return isSymbol(value) ? getSymbol(value).priority : -1;
	}

	public static boolean isOperator(String value) {
		return isSymbol(value) && getSymbol(value).type == TypeEnum.OPERATOR;
	}

	public static boolean isSeparator(String value) {
		return isSymbol(value) && getSymbol(value).type == TypeEnum.SEPARATOR;
	}

	public static boolean isArithmetic(String value) {
		return isSymbol(value) && getSymbol(value).category == CategoryEnum.ARITHMETIC;
	}

	public static boolean isBitwise(String value) {
		return isSymbol(value) && getSymbol(value).category == CategoryEnum.BITWISE;
	}

	public static boolean isRelation(String value) {
		return isSymbol(value) && getSymbol(value).category == CategoryEnum.RELATION;
	}

	public static boolean isLogical(String value) {
		return isSymbol(value) && getSymbol(value).category == CategoryEnum.LOGICAL;
	}

	public static boolean isConditional(String value) {
		return isSymbol(value) && getSymbol(value).category == CategoryEnum.CONDITIONAL;
	}

	public static boolean isAssign(String value) {
		return isSymbol(value) && getSymbol(value).category == CategoryEnum.ASSIGN;
	}

	// 符号值
	public String value;
	// 正则表达式
	public String regex;
	// 需要纠正的书写方式
	public String badRegex;
	// 字符数类型，单字符，还是双字符
	public CharNumberEnum charNumber;
	// 类型,操作符,还是分隔符
	public TypeEnum type;
	// 类别:算术运算符,位运算符,关系运算符,逻辑运算,条件运算符,赋值运算符
	public CategoryEnum category;
	// 优先级
	public int priority;
	// 操作数:左元,右元,二元,多义
	public OperandEnum operand;

	private SymbolEnum(String value, String regex, String badRegex, CharNumberEnum charNumber, TypeEnum type, CategoryEnum category, int priority,
			OperandEnum operand) {
		this.value = value;
		this.regex = regex;
		this.badRegex = badRegex;
		this.charNumber = charNumber;
		this.type = type;
		this.category = category;
		this.priority = priority;
		this.operand = operand;
	}

	private SymbolEnum(String value, String regex, CharNumberEnum charNumber, TypeEnum type, CategoryEnum category, int priority, OperandEnum operand) {
		this(value, regex, null, charNumber, type, category, priority, operand);
	}

	public enum CharNumberEnum {
		SINGLE, // 单字符
		DOUBLE// 双字符
	}

	public enum TypeEnum {
		OPERATOR, // 操作符
		SEPARATOR// 分隔符
	}

	public enum CategoryEnum {
		UNKNOWN, //
		ARITHMETIC, // 计算
		BITWISE, // 移位
		RELATION, // 关系
		LOGICAL, // 逻辑
		CONDITIONAL, // 条件
		ASSIGN // 赋值
	}

	public enum OperandEnum {
		UNKNOWN, //
		LEFT, // 左元
		RIGHT, // 右元
		BINARY, // 二元
		MULTIPLE // 多义
	}

}
