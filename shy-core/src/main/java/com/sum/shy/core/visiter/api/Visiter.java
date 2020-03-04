package com.sum.shy.core.visiter.api;

import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.type.api.IType;

public interface Visiter {

	IType visitField(IClass clazz, IType type, String fieldName);

	IType visitMethod(IClass clazz, IType type, String methodName, List<IType> parameterTypes);

}
