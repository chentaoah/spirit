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

	public NativeType(IClass ctClass, Class<?> clazz, List<Type> genericTypes) {
		super(ctClass);
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public NativeType(IClass ctClass, Class<?> clazz) {
		super(ctClass);
		this.clazz = clazz;
		this.genericTypes = new ArrayList<>();
	}

	public NativeType(IClass ctClass, Type type) {
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

	public Method findMethod(String methodName, List<Type> paramTypes) {
		if(methodName.equals("put"))
			System.out.println();
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == paramTypes.size()) {
				// 假设就是这个方法
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
