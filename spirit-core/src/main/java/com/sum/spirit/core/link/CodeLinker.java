package com.sum.spirit.core.link;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.Compiler;
import com.sum.spirit.api.MemberVisiter;
import com.sum.spirit.api.link.ClassLinker;
import com.sum.spirit.api.link.TypeFactory;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.exception.NoSuchFieldException;
import com.sum.spirit.pojo.exception.NoSuchMethodException;

@Component
public class CodeLinker implements ClassLinker {

	@Autowired
	public Compiler compiler;
	@Autowired
	public MemberVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T toClass(IType type) {
		Assert.isTrue(!type.isArray(), "Array has no class!");// 这里认为数组没有class,也不应该有
		return (T) compiler.findClass(type.getClassName());
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
