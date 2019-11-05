package com.sum.shy.core.entity;

import java.util.List;

public class Param {

	public String type;
	// 泛型参数
	public List<String> genericTypes;

	public String name;

	public Param(String type, String name) {
		this.type = type;
		this.name = name;
	}

}
