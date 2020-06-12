package com.sum.shy.pojo.clazz;

public class IVariable {

	public String blockId;

	public IType type;

	public String name;

	public IVariable() {
	}

	public IVariable(String blockId, IType type, String name) {
		this.blockId = blockId;
		this.type = type;
		this.name = name;
	}

	public IVariable(IType type, String name) {
		this.type = type;
		this.name = name;
	}

}
