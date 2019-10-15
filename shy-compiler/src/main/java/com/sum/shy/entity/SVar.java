package com.sum.shy.entity;

public class SVar {

	public String type;

	public String name;

	public Object value;
	// 方法中的子域
	public String scope;

	public SVar(String type, String name, Object value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

}
