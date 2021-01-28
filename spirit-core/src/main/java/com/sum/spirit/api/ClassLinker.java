package com.sum.spirit.api;

import java.util.List;

import com.sum.spirit.core.visiter.pojo.IType;

public interface ClassLinker {

	boolean canLink(IType type);

	<T> T toClass(IType type);

	int getTypeVariableIndex(IType type, String genericName);

	IType getSuperType(IType type);

	List<IType> getInterfaceTypes(IType type);

	boolean isMoreAbstract(IType abstractType, IType type);

	IType visitField(IType type, String fieldName) throws NoSuchFieldException;

	IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException;

}
