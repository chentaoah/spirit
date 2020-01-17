package com.sum.shy.clazz.api;

import com.sum.shy.type.api.IType;

public abstract class AbsMember extends AbsAnnotated implements Member {
	// 域
	public String scope;
	// 类型
	public IType type;
	// 锁
	public volatile boolean isLock = false;

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public void setType(IType type) {
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
