package com.sum.shy.test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.sum.shy.core.People;

public class Test {

	public static void main(String[] args) {
		try {
			Method method = People.class.getMethod("getName");
			Class<?> class1 = method.getReturnType();
			// 获取泛型参数名
			TypeVariable<?>[] params = class1.getTypeParameters();
//			if (type1 instanceof ParameterizedType) {
//				java.lang.reflect.Type[] genericReturnTypes = ((ParameterizedType) type1).getActualTypeArguments();
//				for (java.lang.reflect.Type genericReturnType : genericReturnTypes) {
//					if (genericReturnType instanceof Class) {
//						System.out.println(genericReturnType);
//					}
//				}
//			}
//			// 获取泛型参数
//			java.lang.reflect.Type methodReturnGenericType = method.getGenericReturnType();
//			if (methodReturnGenericType instanceof ParameterizedType) {
//				java.lang.reflect.Type[] genericReturnTypes = ((ParameterizedType) methodReturnGenericType)
//						.getActualTypeArguments();
//				for (java.lang.reflect.Type genericReturnType : genericReturnTypes) {
//					if (genericReturnType instanceof Class) {
//						System.out.println(genericReturnType);
//					}
//				}
//			}

//			Method method2 = class1.getMethod("get", int.class);
//			Class<?> class2 = method2.getReturnType();
//			// 获取泛型参数
//			java.lang.reflect.Type methodReturnGenericType = method2.getGenericReturnType();
//			if (methodReturnGenericType instanceof ParameterizedType) {
//				java.lang.reflect.Type[] genericReturnTypes = ((ParameterizedType) methodReturnGenericType)
//						.getActualTypeArguments();
//				for (java.lang.reflect.Type genericReturnType : genericReturnTypes) {
//					if (genericReturnType instanceof Class) {
//						System.out.println(genericReturnType);
//					}
//				}
//			}

		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
