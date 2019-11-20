package com.sum.shy.core.entity;

import java.io.File;
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

	/**
	 * 查询引入的类型
	 * 
	 * @param type
	 * @return
	 */
	public boolean isFriends(String className) {
		return files.containsKey(className);
	}

	/**
	 * 查询class
	 * 
	 * @param className
	 */
	public Clazz findClass(String className) {
		return classes.get(className);
	}

	/**
	 * 查询引入的类型
	 * 
	 * @param typeName
	 * @return
	 */
	public String findImport(String typeName) {
		for (String className : files.keySet()) {
			if (className.substring(className.lastIndexOf(".") + 1).equals(typeName)) {
				return className;
			}
		}
		return null;
	}

}
