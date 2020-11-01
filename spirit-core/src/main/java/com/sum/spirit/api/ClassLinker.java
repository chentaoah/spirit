package com.sum.spirit.api;

import java.util.List;

import com.sum.spirit.core.type.IType;
import com.sum.spirit.pojo.exception.NoSuchFieldException;
import com.sum.spirit.pojo.exception.NoSuchMethodException;

public interface ClassLinker {

	<T> T toClass(IType type);

	int getTypeVariableIndex(IType type, String genericName);

	IType getSuperType(IType type);

	List<IType> getInterfaceTypes(IType type);

	IType visitField(IType type, String fieldName) throws NoSuchFieldException;

	IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException;

}
