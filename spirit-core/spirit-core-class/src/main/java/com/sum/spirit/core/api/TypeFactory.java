package com.sum.spirit.core.api;

import java.util.List;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.element.entity.Token;

public interface TypeFactory {

	IType create(String className);

	IType create(String className, IType... genericTypes);

	IType create(String className, List<IType> genericTypes);

	IType createTypeVariable(String genericName);

	IType create(IClass clazz, Token token);

	IType create(IClass clazz, String text);

}
