package com.sum.slimx.pojo.common;

public class Keyword {

	public static final int STRUCT = 1;// 结构级
	public static final int LINE = 2;// 行级
	public static final int TOKEN = 3;// 元素级

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
