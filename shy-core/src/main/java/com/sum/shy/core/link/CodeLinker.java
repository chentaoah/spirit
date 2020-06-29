package com.sum.shy.core.link;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.api.link.MemberLinker;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IField;
import com.sum.shy.pojo.clazz.IMethod;
import com.sum.shy.pojo.clazz.IType;

public class CodeLinker implements MemberLinker {

	public static MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	public static MemberLinker linker = ProxyFactory.get(MemberLinker.class);

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		return type.toClass().getTypeVariableIndex(genericName);
	}

	@Override
	public IType visitField(IType type, String fieldName) {
		IClass clazz = type.toClass();
		IField field = clazz.getField(fieldName);
		if (field != null) {
			IType returnType = visiter.visitMember(clazz, field);
			return factory.populateType(type, returnType);
		}
		return linker.visitField(type.getSuperType(), fieldName);
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		IClass clazz = type.toClass();
		IMethod method = clazz.getMethod(type, methodName, parameterTypes);
		if (method != null) {
			IType returnType = visiter.visitMember(clazz, method);
			return factory.populateType(type, returnType);
		}
		return linker.visitMethod(type.getSuperType(), methodName, parameterTypes);
	}

}
