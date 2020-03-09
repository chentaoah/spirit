package com.sum.shy.core.entity;

import java.util.Map;

import com.sum.shy.core.clazz.IClass;

public class Context {

	public static ThreadLocal<Context> local = new ThreadLocal<>();

	public static Context get() {
		if (local.get() == null) {
			local.set(new Context());
		}
		return local.get();
	}

	public Map<String, IClass> classes;// 此次编译的所有的类

	public boolean contains(String className) {
		return classes.containsKey(className);
	}

	public IClass findClass(String className) {
		return classes.get(className);
	}

	public String findClassName(String typeName) {
		for (String className : classes.keySet()) {
			if (className.endsWith("." + typeName))
				return className;
		}
		return null;
	}

}
