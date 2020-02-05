package com.sum.shy.visiter.api;

import java.util.List;

import com.sum.shy.core.doc.IClass;
import com.sum.shy.type.api.IType;

public interface Visiter {

	IType visitField(IClass clazz, IType type, String fieldName);

	IType visitMethod(IClass clazz, IType type, String methodName, List<IType> paramTypes);

}
