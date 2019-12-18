package com.sum.shy.core.clazz.impl;

import com.sum.shy.core.type.api.Type;

public class Param {

	public Type type;

	public String name;

	public Param(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return type + " " + name;
	}

}
