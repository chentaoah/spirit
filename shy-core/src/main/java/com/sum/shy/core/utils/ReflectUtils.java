package com.sum.shy.core.utils;

import java.lang.reflect.Method;

import com.sum.shy.core.entity.Type;

public class ReflectUtils {

	/**
	 * 不支持java重载 不支持java泛型
	 * 
	 * @param className
	 * @param methodName
	 * @return
	 */
	public static Type getReturnType(String className, String methodName) {
		try {
			Class<?> clazz = Class.forName(className);
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(methodName)) {
					String returnType = method.getReturnType().getSimpleName();
					return new Type(returnType);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Type getReturnType(Type type, String clazzName, String methodName) {
		// TODO Auto-generated method stub
		return null;
	}

}
