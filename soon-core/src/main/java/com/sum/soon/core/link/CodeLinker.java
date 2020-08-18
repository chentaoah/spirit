package com.sum.soon.core.link;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.soon.api.MemberVisiter;
import com.sum.soon.api.link.ClassLinker;
import com.sum.soon.api.link.TypeFactory;
import com.sum.soon.lib.Assert;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.IField;
import com.sum.soon.pojo.clazz.IMethod;
import com.sum.soon.pojo.clazz.IType;
import com.sum.soon.pojo.common.Context;
import com.sum.soon.pojo.exception.NoSuchFieldException;
import com.sum.soon.pojo.exception.NoSuchMethodException;

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
		return factory.populate(type, clazz.getSuperType());
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		IClass clazz = toClass(type);
		List<IType> interfaceTypes = new ArrayList<>();
		for (IType interfaceType : clazz.getInterfaceTypes())
			interfaceTypes.add(factory.populate(type, interfaceType));
		return interfaceTypes;
	}

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		IClass clazz = toClass(type);
		IField field = clazz.getField(fieldName);
		if (field != null)
			return factory.populate(type, visiter.visitMember(clazz, field));
		return null;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		IClass clazz = toClass(type);
		IMethod method = clazz.getMethod(type, methodName, parameterTypes);
		if (method != null)
			return factory.populate(type, visiter.visitMember(clazz, method));
		return null;
	}

}
