package com.sum.shy.api.link;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.clazz.IType;

@Service("adaptive_linker")
public interface MemberLinker {

	IType visitField(IType type, String fieldName);

	IType visitMethod(IType type, String methodName, List<IType> parameterTypes);

}
