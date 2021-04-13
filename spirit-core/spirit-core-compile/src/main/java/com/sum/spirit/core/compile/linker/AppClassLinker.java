package com.sum.spirit.core.compile.linker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IField;
import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.IParameter;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.AppClassLoader;
import com.sum.spirit.core.compile.ClassVisiter;
import com.sum.spirit.core.compile.AppTypeFactory;
import com.sum.spirit.core.compile.deduce.TypeDerivator;

import cn.hutool.core.lang.Assert;

@Component
@Order(-100)
public class AppClassLinker implements ClassLinker {

	@Autowired
	public AppClassLoader classLoader;
	@Autowired
	public AppTypeFactory factory;
	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public TypeDerivator derivator;

	@Override
	public boolean isHandle(IType type) {
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
		return derivator.populate(type, clazz.getSuperType());
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		IClass clazz = toClass(type);
		List<IType> interfaceTypes = new ArrayList<>();
		for (IType interfaceType : clazz.getInterfaceTypes()) {
			interfaceTypes.add(derivator.populate(type, interfaceType));
		}
		return interfaceTypes;
	}

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		IClass clazz = toClass(type);
		IField field = clazz.getField(fieldName);
		if (field != null) {
			return derivator.populate(type, visiter.visitMember(clazz, field));
		}
		return null;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		IClass clazz = toClass(type);
		List<IMethod> methods = clazz.getMethods(methodName);
		IMethod method = Lists.findOne(methods, eachMethod -> matches(type, eachMethod, parameterTypes));
		if (method != null) {
			return derivator.populate(type, visiter.visitMember(clazz, method));
		}
		return null;
	}

	public boolean matches(IType type, IMethod method, List<IType> parameterTypes) {
		if (method.parameters.size() == parameterTypes.size()) {
			for (int index = 0; index < method.parameters.size(); index++) {
				IParameter parameter = method.parameters.get(index);
				IType parameterType = derivator.populate(type, parameter.getType());
				if (!derivator.isMoreAbstract(parameterType, parameterTypes.get(index))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
