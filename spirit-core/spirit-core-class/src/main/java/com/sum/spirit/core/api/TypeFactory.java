package com.sum.spirit.core.api;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.element.entity.Token;

public interface TypeFactory {

	IType create(String className);

	IType create(IClass clazz, Token token);

}
