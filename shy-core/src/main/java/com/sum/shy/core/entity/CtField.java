package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Type;

public class CtField extends AbsElement {
	// 参数名
	public String name;
	// 语句
	public Stmt stmt;

	public CtField(Type type, String name, Stmt stmt, List<String> annotations) {
		this.type = type;
		this.name = name;
		this.stmt = stmt;
		this.annotations = annotations != null ? annotations : new ArrayList<>();
	}

	@Override
	public String toString() {
		return "field --> " + stmt;
	}

}
