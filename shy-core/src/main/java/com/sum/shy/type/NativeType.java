package com.sum.shy.type;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.IClass;
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

	public NativeType(IClass iClass, Class<?> clazz, List<Type> genericTypes) {
		super(iClass);
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public NativeType(IClass iClass, Class<?> clazz) {
		super(iClass);
		this.clazz = clazz;
		this.genericTypes = new ArrayList<>();
	}

	public NativeType(IClass iClass, Type type) {
		super(iClass);
		this.clazz = ReflectUtils.getClass(type.getClassName());
		this.genericTypes = new ArrayList<>();
		for (Type genericType : type.getGenericTypes()) {
			genericTypes.add(new NativeType(iClass, genericType));
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

	public Method findMethod(String methodName, List<Type> paramTypes) {
		if (methodName.equals("put"))
			System.out.println();
		// 如果只有一个方法的话,则直接用该方法
		// 如果存在多个的话,则再判断参数类型是否匹配
		List<Method> methods = new ArrayList<>();
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == paramTypes.size())
				methods.add(method);
		}
		if (methods.size() > 0) {
//			if (methods.size() == 1)
//				return methods.get(0);
			for (Method method : methods) {
				boolean flag = true;
				int count = 0;
				for (Parameter parameter : method.getParameters()) {
					Type type = paramTypes.get(count++);
					Class<?> clazz = ReflectUtils.getClass(type.getClassName());
					if (!(clazz == parameter.getType() || clazz.isAssignableFrom(parameter.getType()))) {
						flag = false;
						break;
					}
				}
				if (flag)
					return method;
			}
		}

		throw new RuntimeException("The method was not found!method:" + methodName);
	}

}
