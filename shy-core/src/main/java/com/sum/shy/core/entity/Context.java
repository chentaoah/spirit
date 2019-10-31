package com.sum.shy.core.entity;

public class Context {

	public static ThreadLocal<Context> local = new ThreadLocal<>();

	public static Context get() {
		if (local.get() == null) {
			local.set(new Context());
		}
		return local.get();
	}

	// 是否debug模式
	public boolean debug;
	// class实体
	public Clazz clazz;
	// 当前域
	public String scope;

}
