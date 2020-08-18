package com.sum.slimx.api.link;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.clazz.IType;
import com.sum.slimx.pojo.exception.NoSuchFieldException;
import com.sum.slimx.pojo.exception.NoSuchMethodException;

@Service("adaptive_linker")
public interface ClassLinker {

	<T> T toClass(IType type);

	int getTypeVariableIndex(IType type, String genericName);

	IType getSuperType(IType type);

	List<IType> getInterfaceTypes(IType type);

	IType visitField(IType type, String fieldName) throws NoSuchFieldException;

	IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException;

}
