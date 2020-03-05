package com.sum.shy.core.clazz;

import com.sum.shy.core.type.api.IType;

public class Variable {

	public String blockId;

	public IType type;

	public String name;

	public Variable() {
	}

	public Variable(String blockId, IType type, String name) {
		this.blockId = blockId;
		this.type = type;
		this.name = name;
	}

}
