package com.sum.shy.core.entity;

public class Keyword {

	public static final int UNKNOWN = -1;// 未知状态

	public static final int STRUCT = 0;// 结构级
	public static final int LINE = 1;// 行级
	public static final int TOKEN = 2;// 元素级

	public int type;

	public String value;

	public Keyword(int type, String value) {
		this.type = type;
		this.value = value;
	}

	public boolean isStruct() {
		return type == STRUCT;
	}

	public boolean isLine() {
		return type == LINE;
	}

	public boolean isToken() {
		return type == TOKEN;
	}

}
