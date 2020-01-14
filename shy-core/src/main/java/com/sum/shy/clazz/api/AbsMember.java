package com.sum.shy.clazz.api;

import com.sum.shy.type.api.Type;

public abstract class AbsMember extends AbsAnnotated implements Member {
	// 域
	public String scope;
	// 类型
	public Type type;
	// 锁
	public volatile boolean isLock = false;

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
		if (isLock)
			throw new RuntimeException("There is a circular dependency!" + toString());
		isLock = true;
	}

	@Override
	public void unLock() {
		isLock = false;
	}

}
