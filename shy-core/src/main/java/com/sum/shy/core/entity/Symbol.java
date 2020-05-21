package com.sum.shy.core.entity;

public class Symbol {

	public static final int NONE = -1;

	public static final int OPERATOR = 0;
	public static final int SEPARATOR = 1;

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int DOUBLE = 2;
	public static final int MULTIPLE = 3;

	public static final int ARITHMETIC = 0;
	public static final int BITWISE = 1;
	public static final int RELATION = 2;
	public static final int LOGICAL = 3;
	public static final int CONDITIONAL = 4;
	public static final int ASSIGN = 5;

	// 类型,操作符,还是分隔符
	public int type;
	// 正则表达式
	public String regex;
	// 需要纠正的书写方式
	public String badRegex;
	// 符号值
	public String value;
	// 优先级
	public int priority;
	// 操作数:左元,右元,二元,多义
	public int operand;
	// 类别:算术运算符,位运算符,关系运算符,逻辑运算,条件运算符,赋值运算符
	public int category;

	public Symbol(int type, String regex, String value, int priority, int operand, int category) {
		this.type = type;
		this.regex = regex;
		this.value = value;
		this.priority = priority;
		this.operand = operand;
		this.category = category;
	}

	public Symbol(int type, String regex, String badRegex, String value, int priority, int operand, int category) {
		this.type = type;
		this.regex = regex;
		this.badRegex = badRegex;
		this.value = value;
		this.priority = priority;
		this.operand = operand;
		this.category = category;
	}

	public boolean isOperator() {
		return type == OPERATOR;
	}

	public boolean isSeparator() {
		return type == SEPARATOR;
	}

	public boolean isLeft() {
		return operand == LEFT;
	}

	public boolean isRight() {
		return operand == RIGHT;
	}

	public boolean isDouble() {
		return operand == DOUBLE;
	}

	public boolean isMultiple() {
		return operand == MULTIPLE;
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

}
