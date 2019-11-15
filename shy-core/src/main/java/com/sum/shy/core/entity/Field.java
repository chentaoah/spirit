package com.sum.shy.core.entity;

import com.sum.shy.core.api.Type;

public class Field {

	public Type type;
	// 参数名
	public String name;
	// 语句
	public Stmt stmt;

	public Field(Type type, String name, Stmt stmt) {
		this.type = type;
		this.name = name;
		this.stmt = stmt;
	}

	@Override
	public String toString() {
		return type.toString();
	}

}
