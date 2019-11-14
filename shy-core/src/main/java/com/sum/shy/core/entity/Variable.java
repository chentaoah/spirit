package com.sum.shy.core.entity;

import com.sum.shy.core.api.Type;

public class Variable {

	public String block;

	public Type type;

	public String name;

	public Variable(String block, Type type, String name) {
		this.block = block;
		this.type = type;
		this.name = name;
	}

}
