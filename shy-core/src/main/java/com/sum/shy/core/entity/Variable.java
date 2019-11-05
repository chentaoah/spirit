package com.sum.shy.core.entity;

import java.util.List;

public class Variable {

	public String block;

	public String type;
	// 泛型参数
	public List<String> genericTypes;

	public String name;

	public Variable(String block, String type, List<String> genericTypes, String name) {
		this.block = block;
		this.type = type;
		this.genericTypes = genericTypes;
		this.name = name;
	}

}
