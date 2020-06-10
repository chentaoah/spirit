package com.sum.shy.api.deducer;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IType;

@Service("member_linker")
public interface MemberLinker {

	IType visitField(IType type, String fieldName);

	IType visitMethod(IType type, String methodName, List<IType> parameterTypes);

}
