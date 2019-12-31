package com.sum.shy.clazz;

import com.sum.shy.type.api.Type;

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
