package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Type {

	public String type;

	public List<Type> genericTypes;

	public Type(String type, List<Type> genericTypes) {
		this.type = type;
		this.genericTypes = genericTypes;
	}

	public Type(String type) {
		this.type = type;
		this.genericTypes = new ArrayList<>();
	}

}
