package com.sum.shy.core.clazz.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.clazz.api.AbsContainer;
import com.sum.shy.core.entity.Line;

public class CtClass extends AbsContainer {

	// 类别
	public String category;
	// 类名
	public String typeName;
	// 父类
	public String superName;
	// 接口(接口继承的接口也在这个里面)
	public List<String> interfaces = new ArrayList<>();
	// class域
	public List<Line> classLines = new ArrayList<>();
	// 内部类( typeName --> CtClass )
	public Map<String, CtClass> innerClasses = new LinkedHashMap<>();

	public String getClassName() {
		return getPackage() + "." + typeName;
	}

	@Override
	public String getId() {
		return getClassName();
	}

}
