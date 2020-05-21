package com.sum.shy.core.entity;

public class Keyword {

	public static final int STRUCT = 0;// 结构级
	public static final int LINE = 1;// 行级
	public static final int TOKEN = 2;// 元素级

	public String value;

	public int type;

	public Keyword(String value, int type) {
		this.value = value;
		this.type = type;
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
