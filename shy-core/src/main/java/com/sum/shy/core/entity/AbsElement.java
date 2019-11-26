package com.sum.shy.core.entity;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.api.Type;

public abstract class AbsElement implements Element {
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
