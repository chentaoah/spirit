package com.sum.spirit.core.compile.deduce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.IParameter;
import com.sum.spirit.core.clazz.entity.IType;

@Component
public class MethodMatcher {

	@Autowired
	public TypeDerivator derivator;

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
