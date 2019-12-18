package com.sum.shy.core.clazz;

import com.sum.shy.core.api.Type;

public interface Element extends Annotated {

	Type getType();

	void setType(Type type);

	void lock();

	void unLock();

}
