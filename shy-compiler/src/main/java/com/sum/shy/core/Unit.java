package com.sum.shy.core;

import java.util.List;

public class Unit {

	public String str;// 语义单元原文

	public String type;// 语义单元类型

	public List<Unit> subunits;// 子单元

	public int priority;// 优先级

	public Unit(String str) {
		this.str = str;

	}

	public Unit(String str, String type) {
		// TODO Auto-generated constructor stub
	}

}
