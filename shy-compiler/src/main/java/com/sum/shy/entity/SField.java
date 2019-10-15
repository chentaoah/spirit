package com.sum.shy.entity;

public class SField {
	// 类型
	public String type;
	// 参数名
	public String name;
	// 初始值
	public Object value;

	public SField(String type, String name, Object value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

}
