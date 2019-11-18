package com.sum.shy.core.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Context {

	public static ThreadLocal<Context> local = new ThreadLocal<>();

	public static Context get() {
		if (local.get() == null) {
			local.set(new Context());
		}
		return local.get();
	}

	// 所有被解析的结构体
	public Map<String, File> files;
	// 所有被解析的结构体
	public Map<String, Clazz> classes;
	// 当前读取的class实体
	public Clazz clazz;
	// 当前域
	public String scope;
	// 方法解析链,不能相互嵌套依赖
	public List<String> dependencies = new ArrayList<>();

	public String findImport(String type) {
		for (String className : files.keySet()) {
			if (className.substring(className.lastIndexOf(".") + 1).equals(type)) {
				return className;
			}
		}
		return null;
	}

}
