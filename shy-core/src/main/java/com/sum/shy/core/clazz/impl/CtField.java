package com.sum.shy.core.clazz.impl;

import java.util.List;

import com.sum.shy.core.clazz.api.AbsElement;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.type.api.Type;

public class CtField extends AbsElement {
	// 参数名
	public String name;
	// 语句
	public Stmt stmt;

	public CtField(String scope, Type type, String name, Stmt stmt, List<String> annotations) {
		// 注解
		setAnnotations(annotations);
		// 域
		setScope(scope);
		// 类型
		setType(type);

		this.name = name;
		this.stmt = stmt;

	}

	@Override
	public String toString() {
		return "field --> " + stmt;
	}

}
