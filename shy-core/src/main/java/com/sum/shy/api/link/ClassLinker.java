package com.sum.shy.api.link;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.clazz.IType;

@Service("adaptive_linker")
public interface ClassLinker {

	<T> T toClass(IType type);

	int getTypeVariableIndex(IType type, String genericName);

	IType getSuperType(IType type);

	List<IType> getInterfaceTypes(IType type);

	IType visitField(IType type, String fieldName);

	IType visitMethod(IType type, String methodName, List<IType> parameterTypes);

}
