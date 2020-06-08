package com.sum.shy.core.pojo;

public class Symbol {

	public static final int UNKNOWN = -1;// 未知状态

	public static final int OPERATOR = 0;// 操作符
	public static final int SEPARATOR = 1;// 分隔符

	public static final int SINGLE = 0;// 单字符
	public static final int DOUBLE = 1;// 双字符

	public static final int ARITHMETIC = 0;// 计算
	public static final int BITWISE = 1;// 移位
	public static final int RELATION = 2;// 关系
	public static final int LOGICAL = 3;// 逻辑
	public static final int CONDITIONAL = 4;// 条件
	public static final int ASSIGN = 5;// 赋值

	public static final int LEFT = 0;// 左元
	public static final int RIGHT = 1;// 右元
	public static final int BINARY = 2;// 二元
	public static final int MULTIPLE = 3;// 多义

	// 符号值
	public String value;
	// 正则表达式
	public String regex;
	// 需要纠正的书写方式
	public String badRegex;
	// 类型,操作符,还是分隔符
	public int type;
	// 字符数类型，单字符，还是双字符
	public int charType;
	// 类别:算术运算符,位运算符,关系运算符,逻辑运算,条件运算符,赋值运算符
	public int category;
	// 优先级
	public int priority;
	// 操作数:左元,右元,二元,多义
	public int operand;

	public Symbol(String value, String regex, int type, int charType, int category, int priority, int operand) {
		this(value, regex, null, type, charType, category, priority, operand);
	}

	public Symbol(String value, String regex, String badRegex, int type, int charType, int category, int priority, int operand) {
		this.value = value;
		this.regex = regex;
		this.badRegex = badRegex;
		this.type = type;
		this.charType = charType;
		this.category = category;
		this.priority = priority;
		this.operand = operand;
	}

	public boolean isOperator() {
		return type == OPERATOR;
	}

	public boolean isSeparator() {
		return type == SEPARATOR;
	}

	public boolean isSingle() {
		return charType == SINGLE;
	}

	public boolean isDouble() {
		return charType == DOUBLE;
	}

	public boolean isArithmetic() {
		return category == ARITHMETIC;
	}

	public boolean isBitwise() {
		return category == BITWISE;
	}

	public boolean isRelation() {
		return category == RELATION;
	}

	public boolean isLogical() {
		return category == LOGICAL;
	}

	public boolean isConditional() {
		return category == CONDITIONAL;
	}

	public boolean isAssign() {
		return category == ASSIGN;
	}

	public boolean isLeft() {
		return operand == LEFT;
	}

	public boolean isRight() {
		return operand == RIGHT;
	}

	public boolean isBinary() {
		return operand == BINARY;
	}

	public boolean isMultiple() {
		return operand == MULTIPLE;
	}

}
