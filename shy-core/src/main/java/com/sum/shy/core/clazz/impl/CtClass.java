package com.sum.shy.core.clazz.impl;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.api.AbsLinkable;
import com.sum.shy.core.entity.Line;

public class CtClass extends AbsLinkable {
	// 类名
	public String typeName;
	// 父类
	public String superName;
	// 接口(接口继承的接口也在这个里面)
	public List<String> interfaces = new ArrayList<>();
	// class域
	public List<Line> classLines = new ArrayList<>();

	public String getClassName() {
		return getPackage() + "." + typeName;
	}

}
