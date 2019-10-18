package com.sum.shy.entity;

import java.util.ArrayList;
import java.util.List;

public class SClass {

	// 包名
	public String packageStr;
	// 引入
	public List<String> importStrs = new ArrayList<>();
	// 类名
	public String className;
	// 父类
	public String superClass;
	// 接口
	public List<String> interfaces = new ArrayList<>();
	// 静态字段
	public List<SField> staticFields = new ArrayList<>();
	// 静态方法
	public List<SMethod> staticMethods = new ArrayList<>();
	// 字段
	public List<SField> fields = new ArrayList<>();
	// 方法
	public List<SMethod> methods = new ArrayList<>();
	// class域
	public List<String> classLines;

}
