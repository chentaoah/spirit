package com.sum.shy.core.entity;

import java.util.List;

public class Field {
	// 类型
	public String type;
	// 泛型参数
	public List<String> genericTypes;
	// 参数名
	public String name;
	// 语句
	public Stmt stmt;

	public Field(String type, List<String> genericTypes, String name, Stmt stmt) {
		this.type = type;
		this.genericTypes = genericTypes;
		this.name = name;
		this.stmt = stmt;
	}

}
