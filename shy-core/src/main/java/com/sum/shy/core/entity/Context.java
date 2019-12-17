package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Context {

	public static ThreadLocal<Context> local = new ThreadLocal<>();

	public static Context get() {
		if (local.get() == null) {
			local.set(new Context());
		}
		return local.get();
	}

	// 所有友元
	public Set<String> friends;
	// 所有类，包括内部类
	public Map<String, CtClass> classes;
	// 解析到的注解上下文
	public List<String> annotations = new ArrayList<>();

	/**
	 * 查询引入的类型
	 * 
	 * @param typeName
	 * @return
	 */
	public String findFriend(String typeName) {
		for (String className : friends) {
			if (className.endsWith("." + typeName)) {
				return className;
			}
		}
		return null;
	}

	/**
	 * 是否友元
	 * 
	 * @param className
	 * @return
	 */
	public boolean isFriend(String className) {
		return friends.contains(className);
	}

	/**
	 * 是否包含这个类
	 * 
	 * @param className
	 * @return
	 */
	public boolean contains(String className) {
		return classes.containsKey(className);
	}

	/**
	 * 查询class
	 * 
	 * @param className
	 */
	public CtClass findClass(String className) {
		return classes.get(className);
	}

	/**
	 * 查询class
	 * 
	 * @param typeName
	 */
//	public String findClassName(String typeName) {
//		for (String className : classes.keySet()) {
//			if (className.endsWith("." + typeName)) {
//				return className;
//			}
//		}
//		return null;
//	}

	/**
	 * 返回上下文中等待处理的注解
	 * 
	 * @return
	 */
	public List<String> getAnnotations() {
		List<String> result = annotations;
		annotations = new ArrayList<>();
		return result;
	}

}
