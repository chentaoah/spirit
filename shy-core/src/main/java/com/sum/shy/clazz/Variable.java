package com.sum.shy.clazz;

import com.sum.shy.type.api.IType;

public class Variable extends Param {

	public String block;

	public Variable(String block, IType type, String name) {
		super(type, name);
		this.block = block;
	}

}
