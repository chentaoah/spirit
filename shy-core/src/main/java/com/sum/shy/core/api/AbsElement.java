package com.sum.shy.core.api;

import java.util.List;

public abstract class AbsElement implements Element {
	// 注解
	public List<String> annotations;
	// 类型
	public Type type;
	// 锁
	public volatile boolean isLock = false;

	@Override
	public List<String> getAnnotations() {
		return annotations;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void lock() {
		if (isLock) {
			throw new RuntimeException("There is a circular dependency!" + toString());
		}
		isLock = true;
	}

	@Override
	public void unLock() {
		isLock = false;
	}

}
