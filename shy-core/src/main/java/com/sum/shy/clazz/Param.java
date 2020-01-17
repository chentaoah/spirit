package com.sum.shy.clazz;

import com.sum.shy.type.api.IType;

public class Param {

	public IType type;

	public String name;

	public Param(IType type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return type + " " + name;
	}

}
