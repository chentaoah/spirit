package com.sum.shy.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Class {

	// 包名
	public String packageStr;
	// 引入
	public List<String> importStrs = new ArrayList<>();
	// 预处理
	public Map<String, String> defTypes = new HashMap<>();
	// 类名
	public String className;
	// 父类
	public String superName;
	// 接口
	public List<String> interfaces = new ArrayList<>();
	// 静态字段
	public List<Field> staticFields = new ArrayList<>();
	// 静态方法
	public List<Method> staticMethods = new ArrayList<>();
	// 字段
	public List<Field> fields = new ArrayList<>();
	// 方法
	public List<Method> methods = new ArrayList<>();
	// class域
	public List<String> classLines;

}
