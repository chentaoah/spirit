package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.element.Token;

@Service("type_factory")
public interface TypeFactory {

	IType create(String className);

	IType create(Class<?> clazz);

	IType create(Class<?> clazz, List<IType> genericTypes);

	IType create(IClass clazz, String text);

	IType create(IClass clazz, Token token);

}
