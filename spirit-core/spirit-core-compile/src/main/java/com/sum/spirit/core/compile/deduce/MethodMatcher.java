package com.sum.spirit.core.compile.deduce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.IParameter;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.linker.TypeFactory;

@Component
public class MethodMatcher {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public ClassLinker linker;

	public IMethod getMethod(IClass clazz, IType type, String methodName, List<IType> parameterTypes) {
		return Lists.findOne(clazz.methods, method -> matches(method, type, methodName, parameterTypes));
	}

	public boolean matches(IMethod method, IType type, String methodName, List<IType> parameterTypes) {
		if (method.getName().equals(methodName) && method.parameters.size() == parameterTypes.size()) {
			int count = 0;
			for (IParameter parameter : method.parameters) {
				IType parameterType = factory.populate(type, parameter.getType());
				if (!linker.isMoreAbstract(parameterType, parameterTypes.get(count++))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
