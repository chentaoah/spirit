package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Type {

	public String name;

	public List<Type> genericTypes;

	public Type(String type, List<Type> genericTypes) {
		this.name = type;
		this.genericTypes = genericTypes;
	}

	public Type(String type) {
		this.name = type;
		this.genericTypes = new ArrayList<>();
	}

}
