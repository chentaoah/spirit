package com.sum.shy.visiter.api;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.type.api.Type;

public interface Visiter {

	Type visitField(IClass clazz, Type type, String fieldName);

	Type visitMethod(IClass clazz, Type type, String methodName, List<Type> paramTypes);

}
