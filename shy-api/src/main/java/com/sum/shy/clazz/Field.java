package com.sum.shy.clazz;

import java.util.List;

import com.sum.shy.sentence.Sentence;

public class Field {
	// 类型
	public String type;
	// 泛型参数
	public List<String> genericTypes;
	// 参数名
	public String name;
	// 语句
	public Sentence sentence;

	public Field(String type, List<String> genericTypes, String name, Sentence sentence) {
		this.type = type;
		this.genericTypes = genericTypes;
		this.name = name;
		this.sentence = sentence;
	}

}
