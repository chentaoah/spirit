package com.sum.shy.core.entity;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.utils.ReflectUtils;

/**
 * 本地类型
 * 
 * @author chentao
 *
 */
public class NativeType extends AbsType {

	public Class<?> clazz;// 类名

	public NativeType(Class<?> clazz, List<Type> genericTypes) {
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public NativeType(Class<?> clazz) {
		this.clazz = clazz;
		this.genericTypes = new ArrayList<>();
	}

	public NativeType(Type type) {
		this.clazz = ReflectUtils.getClass(type.getClassName());
		this.genericTypes = new ArrayList<>();
		for (Type genericType : type.getGenericTypes()) {
			genericTypes.add(new NativeType(genericType));
		}
	}

	@Override
	public String getClassName() {
		return clazz.getName();
	}

	@Override
	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	public Method findMethod(String methodName, List<Type> parameterTypes) {
		// 遍历方法
		for (Method method : clazz.getMethods()) {
			// 方法名相同
			if (method.getName().equals(methodName)) {
				// 参数个数相同
				if (method.getParameterCount() == parameterTypes.size()) {
					// 假设就是这个方法
					boolean flag = true;
					int count = 0;
					// 遍历参数类型
					for (Parameter parameter : method.getParameters()) {
						if (!parameter.getType().getName().equals(parameterTypes.get(count++).getClassName())) {
							flag = false;
							break;
						}
					}
					if (flag)
						return method;
				}
			}
		}
		throw new RuntimeException("The method was not found!method:" + methodName);
	}

}
