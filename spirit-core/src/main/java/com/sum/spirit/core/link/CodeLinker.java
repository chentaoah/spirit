package com.sum.spirit.core.link;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.ClassVisiter;
import com.sum.spirit.core.AppClassLoader;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.clazz.impl.IField;
import com.sum.spirit.pojo.clazz.impl.IMethod;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.utils.SpringUtils;

import cn.hutool.core.lang.Assert;

@Component
@Order(-100)
public class CodeLinker implements ClassLinker {

	@Autowired
	public AppClassLoader classLoader;
	@Autowired
	public TypeFactory factory;
	@Autowired
	public ClassVisiter visiter;

	@Override
	public boolean canLink(IType type) {
		return !type.isNative();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T toClass(IType type) {
		Assert.isTrue(!type.isArray(), "Array has no class!");// 这里认为数组没有class,也不应该有
		return (T) classLoader.findClass(type.getClassName());
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
		for (IType interfaceType : clazz.getInterfaceTypes()) {
			interfaceTypes.add(factory.populate(type, interfaceType));
		}
		return interfaceTypes;
	}

	@Override
	public boolean isMoreAbstract(IType abstractType, IType type) {
		ClassLinker linker = SpringUtils.getBean(ClassLinker.class);
		return linker.isMoreAbstract(abstractType, type);
	}

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		IClass clazz = toClass(type);
		IField field = clazz.getField(fieldName);
		if (field != null) {
			return factory.populate(type, visiter.visitMember(clazz, field));
		}
		return null;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		IClass clazz = toClass(type);
		IMethod method = clazz.getMethod(type, methodName, parameterTypes);
		if (method != null) {
			return factory.populate(type, visiter.visitMember(clazz, method));
		}
		return null;
	}

}
