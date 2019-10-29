package com.sum.shy.core.entity;

public class Token {

	public String type;

	public Object value;

	public Token(String type, Object value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
