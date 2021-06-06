package com.gitee.spirit.core.compile.linker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.IParameter;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.AppTypeDerivator;

@Component
public class AppMethodMatcher {

	@Autowired
	public AppTypeDerivator derivator;

	public Integer getMethodScore(IType type, IMethod method, List<IType> parameterTypes) {
		if (method.parameters.size() != parameterTypes.size()) {
			return null;
		}
		Integer finalScore = 0;
		int index = 0;
		for (IType parameterType : parameterTypes) {
			IParameter parameter = method.parameters.get(index++);
			IType methodParameterType = derivator.populate(type, parameter.getType());
			Integer scope = derivator.getAbstractScore(methodParameterType, parameterType);
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
