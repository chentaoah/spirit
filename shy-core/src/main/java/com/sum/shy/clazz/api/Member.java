package com.sum.shy.clazz.api;

import com.sum.shy.type.api.IType;

public interface Member {

	IType getType();

	void setType(IType type);

	void lock();

	void unLock();

}
