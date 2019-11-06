package com.sum.shy.core.entity;

public class Variable {

	public String block;

	public Type type;

	public String name;

	public Variable(String block, Type type, String name) {
		this.block = block;
		this.type = type;
		this.name = name;
	}

	public Variable(String block, String type, String name) {
		this.block = block;
		this.type = new Type(type);
		this.name = name;
	}

}
