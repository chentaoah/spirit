package com.sum.spirit.core.api;

import com.sum.spirit.core.clazz.entity.IClass;

public interface ClassVisiter {

	void prepareForVisit(IClass clazz);

	void visitClass(IClass clazz);

}
