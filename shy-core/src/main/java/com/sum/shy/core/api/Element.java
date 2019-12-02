package com.sum.shy.core.api;

public interface Element extends Annotated {

	Type getType();

	void setType(Type type);

	void lock();

	void unLock();

}
