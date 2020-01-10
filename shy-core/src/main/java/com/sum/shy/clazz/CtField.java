package com.sum.shy.clazz;

import java.util.List;

import com.sum.shy.clazz.api.AbsMember;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.type.api.Type;

public class CtField extends AbsMember {
	// 参数名
	public String name;
	// 语句
	public Stmt stmt;

	public CtField(List<String> annotations, String scope, Type type, String name, Stmt stmt) {
		// 注解
		setAnnotations(annotations);
		// 域
		this.scope = scope;
		// 类型
		this.type = type;

		this.name = name;
		this.stmt = stmt;

	}

	@Override
	public String toString() {
		return "field --> " + stmt;
	}

}
