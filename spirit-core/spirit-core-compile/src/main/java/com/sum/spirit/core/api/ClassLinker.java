package com.sum.spirit.core.api;

import java.util.List;

import com.sum.spirit.core.clazz.entity.IType;

public interface ClassLinker {

	boolean isHandle(IType type);

	<T> T toClass(IType type);

	int getTypeVariableIndex(IType type, String genericName);

	IType getSuperType(IType type);

	List<IType> getInterfaceTypes(IType type);

	IType visitField(IType type, String fieldName) throws NoSuchFieldException;

	IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException;

}
