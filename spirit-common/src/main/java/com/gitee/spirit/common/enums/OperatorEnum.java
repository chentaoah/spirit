package com.gitee.spirit.common.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum OperatorEnum {

	INCREASE("++", OperatorTypeEnum.ARITHMETIC, 40, OperandEnum.LEFT), //
	DECREASE("--", OperatorTypeEnum.ARITHMETIC, 40, OperandEnum.LEFT), //
	NEGATE("!", OperatorTypeEnum.LOGICAL, 40, OperandEnum.RIGHT), //
	MULTIPLY("*", OperatorTypeEnum.ARITHMETIC, 35, OperandEnum.BINARY), //
	DIVIDE("/", OperatorTypeEnum.ARITHMETIC, 35, OperandEnum.BINARY), //
	REMIND("%", OperatorTypeEnum.ARITHMETIC, 35, OperandEnum.BINARY), //
	ADD("+", OperatorTypeEnum.ARITHMETIC, 30, OperandEnum.BINARY), //
	SUBTRACT("-", OperatorTypeEnum.ARITHMETIC, 30, OperandEnum.MULTIPLE), //
	LEFT_SHIFT("<<", OperatorTypeEnum.BITWISE, 25, OperandEnum.BINARY), //
	RIGHT_SHIFT(">>", OperatorTypeEnum.BITWISE, 25, OperandEnum.BINARY), //
	BITWISE_NOT("~", OperatorTypeEnum.BITWISE, 20, OperandEnum.RIGHT), //
	BITWISE_AND("&", OperatorTypeEnum.BITWISE, 20, OperandEnum.BINARY), //
	BITWISE_OR("|", OperatorTypeEnum.BITWISE, 20, OperandEnum.BINARY), //
	BITWISE_XOR("^", OperatorTypeEnum.BITWISE, 20, OperandEnum.BINARY), //
	EQUAL("==", OperatorTypeEnum.RELATION, 15, OperandEnum.BINARY), //
	UNEQUAL("!=", OperatorTypeEnum.RELATION, 15, OperandEnum.BINARY), //
	LESS_EQUAL("<=", OperatorTypeEnum.RELATION, 15, OperandEnum.BINARY), //
	MORE_EQUAL(">=", OperatorTypeEnum.RELATION, 15, OperandEnum.BINARY), //
	LESS("<", OperatorTypeEnum.RELATION, 15, OperandEnum.BINARY), //
	MORE(">", OperatorTypeEnum.RELATION, 15, OperandEnum.BINARY), //
	AND("&&", OperatorTypeEnum.LOGICAL, 10, OperandEnum.BINARY), //
	OR("||", OperatorTypeEnum.LOGICAL, 10, OperandEnum.BINARY), //
	QUESTION_MARK("?", OperatorTypeEnum.CONDITIONAL, 5, OperandEnum.BINARY), //
	ASSIGN("=", OperatorTypeEnum.ASSIGN, 5, OperandEnum.BINARY); //

	public static final Map<String, OperatorEnum> OPERATOR_MAP = new ConcurrentHashMap<>();

	static {
		for (OperatorEnum operatorEnum : values()) {
			OPERATOR_MAP.put(operatorEnum.value, operatorEnum);
		}
	}

	public static boolean isOperator(String value) {
		return OPERATOR_MAP.containsKey(value);
	}

	public static OperatorEnum getOperator(String value) {
		return OPERATOR_MAP.get(value);
	}

	public static int getPriority(String value) {
		return isOperator(value) ? getOperator(value).priority : -1;
	}

	public static boolean isArithmetic(String value) {
		return isOperator(value) && getOperator(value).category == OperatorTypeEnum.ARITHMETIC;
	}

	public static boolean isBitwise(String value) {
		return isOperator(value) && getOperator(value).category == OperatorTypeEnum.BITWISE;
	}

	public static boolean isRelation(String value) {
		return isOperator(value) && getOperator(value).category == OperatorTypeEnum.RELATION;
	}

	public static boolean isLogical(String value) {
		return isOperator(value) && getOperator(value).category == OperatorTypeEnum.LOGICAL;
	}

	public static boolean isConditional(String value) {
		return isOperator(value) && getOperator(value).category == OperatorTypeEnum.CONDITIONAL;
	}

	public static boolean isAssign(String value) {
		return isOperator(value) && getOperator(value).category == OperatorTypeEnum.ASSIGN;
	}

	// 符号值
	public String value;
	// 类别:算术运算符,位运算符,关系运算符,逻辑运算,条件运算符,赋值运算符
	public OperatorTypeEnum category;
	// 优先级
	public int priority;
	// 操作数:左元,右元,二元,多义
	public OperandEnum operand;

	private OperatorEnum(String value, OperatorTypeEnum category, int priority, OperandEnum operand) {
		this.value = value;
		this.category = category;
		this.priority = priority;
		this.operand = operand;
	}

	public enum OperatorTypeEnum {
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
