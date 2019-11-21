package com.sum.shy.core.entity;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.api.Type;

public class CtField implements Element {

	public Type type;
	// 参数名
	public String name;
	// 语句
	public Stmt stmt;

	public CtField(Type type, String name, Stmt stmt) {
		this.type = type;
		this.name = name;
		this.stmt = stmt;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type.toString();
	}

}
