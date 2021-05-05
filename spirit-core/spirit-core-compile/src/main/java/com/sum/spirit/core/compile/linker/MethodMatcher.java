package com.sum.spirit.core.compile.linker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.clazz.entity.IMethod;
import com.sum.spirit.core.clazz.entity.IParameter;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.deduce.TypeDerivator;

@Component
public class MethodMatcher {

	@Autowired
	public TypeDerivator derivator;

	public Integer getMethodScore(IType type, IMethod method, List<IType> parameterTypes) {
		if (method.parameters.size() != parameterTypes.size()) {
			return null;
		}
		Integer finalScore = 0;
		int index = 0;
		for (IParameter parameter : method.parameters) {
			IType parameterType = derivator.populate(type, parameter.getType());
			Integer scope = derivator.getAbstractScore(parameterType, parameterTypes.get(index++));
			if (scope != null) {
				finalScore += scope;
			} else {
				finalScore = null;
				break;
			}
		}
		return finalScore;
	}
}
