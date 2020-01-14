package com.sum.shy.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.api.AbsLinkable;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;

public class IClass extends AbsLinkable {
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

	public boolean isInterface() {
		return Constants.INTERFACE_KEYWORD.equals(category);
	}

	public boolean isAbstract() {
		return Constants.ABSTRACT_KEYWORD.equals(category);
	}

	public boolean isClass() {
		return Constants.CLASS_KEYWORD.equals(category);
	}

	public String getClassName() {
		return getPackage() + "." + typeName;
	}

}
