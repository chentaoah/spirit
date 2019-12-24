package com.sum.shy.core.clazz.api;

import com.sum.shy.core.type.api.Type;

public interface Member extends Annotated {

	Type getType();

	void setType(Type type);

	void lock();

	void unLock();

}