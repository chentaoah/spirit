package com.sum.shy.core.entity;

public class Symbol {

	public static final int OPERATOR = 0;
	public static final int SEPARATOR = 1;

	public static final int NONE = -1;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int DOUBLE = 2;
	public static final int MULTIPLE = 3;

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
	// 类别,左元,右元,二元,多义
	public int category;

	public Symbol(int type, String regex, String value, int priority, int category) {
		this.type = type;
		this.regex = regex;
		this.value = value;
		this.priority = priority;
		this.category = category;
	}

	public Symbol(int type, String regex, String badRegex, String value, int priority, int category) {
		this.type = type;
		this.regex = regex;
		this.badRegex = badRegex;
		this.value = value;
		this.priority = priority;
		this.category = category;
	}

}
