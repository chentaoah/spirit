package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Type {

	public String name;

	public List<Type> genericTypes;

	public NativeType nativeType;

	public Type(String name, List<Type> genericTypes) {
		this.name = name;
		this.genericTypes = genericTypes;
	}

	public Type(String name) {
		this.name = name;
		this.genericTypes = new ArrayList<>();
	}

	public Type(NativeType nativeType) {
		this.nativeType = nativeType;
	}

}
