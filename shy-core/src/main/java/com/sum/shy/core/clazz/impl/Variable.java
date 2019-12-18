package com.sum.shy.core.clazz.impl;

import com.sum.shy.core.type.api.Type;

public class Variable extends Param {

	public String block;

	public Variable(String block, Type type, String name) {
		super(type, name);
		this.block = block;
	}

}
