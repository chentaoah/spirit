package com.sum.shy.core.entity;

public class Field {

	public NativeType type;
	// 参数名
	public String name;
	// 语句
	public Stmt stmt;

	public Field(NativeType type, String name, Stmt stmt) {
		this.type = type;
		this.name = name;
		this.stmt = stmt;
	}

}
