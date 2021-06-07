package com.gitee.spirit.output.java.linker;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.output.java.ExtTypeDerivator;
import com.gitee.spirit.output.java.ExtTypeFactory;
import com.gitee.spirit.output.java.utils.ReflectUtils;

@Component
public class ExtMethodMatcher {

	@Autowired
	public ExtTypeFactory factory;
	@Autowired
	public ExtTypeDerivator derivator;

	public boolean checkParameterCount(Method method, List<IType> parameterTypes) {
		if (!ReflectUtils.isIndefinite(method) && parameterTypes.size() == method.getParameterCount()) {// 不是不定项，那么参数个数相等
			return true;

		} else if (ReflectUtils.isIndefinite(method) && parameterTypes.size() >= method.getParameterCount() - 1) {// 不定项，则参数大于等于不定项-1
			return true;
		}
		return false;
	}

	public Integer getMethodScore(IType type, Method method, List<IType> parameterTypes) {
		if (!checkParameterCount(method, parameterTypes)) {
			return null;
		}
		Integer finalScore = 0;
		int index = 0;
		for (IType parameterType : parameterTypes) {
			int idx = index < method.getParameterCount() - 1 ? index : method.getParameterCount() - 1;// 保证索引不会溢出
			Parameter parameter = method.getParameters()[idx];// 分为两种情况，一种是最后一个参数之前的，一种是最后一个参数
			IType nativeParameterType = factory.create(parameter.getParameterizedType());// 获取本地参数类型
			if (idx == method.getParameterCount() - 1 && ReflectUtils.isIndefinite(parameter)) {// 如果最后一个参数，而且是不定项参数，则取数组里的类型
				nativeParameterType = nativeParameterType.toTarget();
			}
			nativeParameterType = derivator.populateParameter(type, parameterType, nativeParameterType);// 填充类型里的泛型参数
			Integer scope = derivator.getAbstractScore(nativeParameterType, parameterType);
			if (scope != null) {
				finalScore += scope;
			} else {
				finalScore = null;
				break;
			}
			index++;
		}
		return finalScore;
	}

}
