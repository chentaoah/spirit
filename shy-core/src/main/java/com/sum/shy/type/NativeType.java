package com.sum.shy.type;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.CtClass;
import com.sum.shy.type.api.AbsType;
import com.sum.shy.type.api.Type;
import com.sum.shy.utils.ReflectUtils;

/**
 * 本地类型
 * 
 * @author chentao
 *
 */
public class NativeType extends AbsType {

	public Class<?> clazz;// 类名

	public NativeType(CtClass ctClass, Class<?> clazz, List<Type> genericTypes) {
		super(ctClass);
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public NativeType(CtClass ctClass, Class<?> clazz) {
		super(ctClass);
		this.clazz = clazz;
		this.genericTypes = new ArrayList<>();
	}

	public NativeType(CtClass ctClass, Type type) {
		super(ctClass);
		this.clazz = ReflectUtils.getClass(type.getClassName());
		this.genericTypes = new ArrayList<>();
		for (Type genericType : type.getGenericTypes()) {
			genericTypes.add(new NativeType(ctClass, genericType));
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