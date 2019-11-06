package com.sum.shy.core.entity;

public class Param {

	public Type type;

	public String name;

	public Param(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	public Param(String type, String name) {
		this.type = new Type(type);
		this.name = name;
	}

}
