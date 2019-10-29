package com.sum.shy.core.entity;

import java.util.List;

public class Context {

	public static ThreadLocal<Context> local = new ThreadLocal<>();

	public static Context get() {
		if (local.get() == null) {
			local.set(new Context());
		}
		return local.get();
	}

	// class实体
	public Clazz clazz;
	// 当前域
	public String scope;
	// 所有行
	public List<String> lines;
	// 当前索引
	public int lineNumber;
	// 当前行
	public String line;

}
