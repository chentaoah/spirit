package com.sum.shy.clazz.api;

import com.sum.shy.type.api.Type;

public interface Member extends Annotated {

	Type getType();

	void setType(Type type);

	void lock();

	void unLock();

}