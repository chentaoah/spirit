package com.sum.shy.entity;

public class Field {
	// 类型
	public String type;
	// 参数名
	public String name;
	// 语句
	public Sentence sentence;

	public Field(String type, String name, Sentence sentence) {
		this.type = type;
		this.name = name;
		this.sentence = sentence;
	}

}
