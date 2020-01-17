package com.sum.shy.clazz;

import java.util.List;

import com.google.common.base.Joiner;
import com.sum.shy.clazz.api.AbsAnnotated;
import com.sum.shy.type.api.IType;

public class Param extends AbsAnnotated {

	public IType type;

	public String name;

	public Param(List<String> annotations, IType type, String name) {
		// 注解
		setAnnotations(annotations);
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		if (getAnnotations().size() > 0) {
			return Joiner.on(" ").join(getAnnotations()) + " " + type + " " + name;
		} else {
			return type + " " + name;
		}

	}

}
