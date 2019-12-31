package com.sum.shy.clazz;

import com.sum.shy.type.api.Type;

public class Variable extends Param {

	public String block;

	public Variable(String block, Type type, String name) {
		super(type, name);
		this.block = block;
	}

}
