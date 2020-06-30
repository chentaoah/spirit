package com.sum.shy.core.link;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.api.link.ClassLinker;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IField;
import com.sum.shy.pojo.clazz.IMethod;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.Context;

public class CodeLinker implements ClassLinker {

	public static MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	public static ClassLinker linker = ProxyFactory.get(ClassLinker.class);

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	@SuppressWarnings("unchecked")
	public <T> T toClass(IType type) {
		Assert.isTrue(!type.isArray(), "Array has no class!");// 这里认为数组没有class,也不应该有
		return (T) Context.get().findClass(type.getClassName());
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		IClass clazz = toClass(type);
		return clazz.getTypeVariableIndex(genericName);
	}

	@Override
	public IType getSuperType(IType type) {
		IClass clazz = toClass(type);
		return factory.populateType(type, clazz.getSuperType());
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		IClass clazz = toClass(type);
		List<IType> interfaceTypes = new ArrayList<>();
		for (IType interfaceType : clazz.getInterfaceTypes())
			interfaceTypes.add(factory.populateType(type, interfaceType));
		return interfaceTypes;
	}

	@Override
	public IType visitInternalField(IType type, String fieldName) {
		return visitField(type, fieldName);
	}

	@Override
	public IType visitInternalMethod(IType type, String methodName, List<IType> parameterTypes) {
		return visitMethod(type, methodName, parameterTypes);
	}

	@Override
	public IType visitField(IType type, String fieldName) {
		IClass clazz = toClass(type);
		IField field = clazz.getField(fieldName);
		if (field != null) {
			IType returnType = visiter.visitMember(clazz, field);
			return factory.populateType(type, returnType);
		}
		return linker.visitField(type.getSuperType(), fieldName);
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		IClass clazz = toClass(type);
		IMethod method = clazz.getMethod(type, methodName, parameterTypes);
		if (method != null) {
			IType returnType = visiter.visitMember(clazz, method);
			return factory.populateType(type, returnType);
		}
		return linker.visitMethod(type.getSuperType(), methodName, parameterTypes);
	}

}
