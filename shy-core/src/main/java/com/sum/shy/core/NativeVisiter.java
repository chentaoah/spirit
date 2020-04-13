package com.sum.shy.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.utils.ReflectUtils;
import com.sum.shy.lib.StringUtils;

public class NativeVisiter {

	public static IType visitField(IType type, String fieldName) {
		Class<?> clazz = ReflectUtils.getClass(type.getClassName());
		try {
			if (StringUtils.isNotEmpty(fieldName)) {
				Field field = clazz.getField(fieldName);
				return visitMember(type, field.getGenericType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		try {
			if (StringUtils.isNotEmpty(methodName)) {
				Method method = findMethod(type, methodName, parameterTypes);
				return visitMember(type, method.getGenericReturnType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		Class<?> clazz = ReflectUtils.getClass(type.getClassName());
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				boolean flag = true;
				int count = 0;
				for (Parameter parameter : method.getParameters()) {
					IType paramType = NativeVisiter.visitMember(type, parameter.getParameterizedType());
					IType parameterType = parameterTypes.get(count++);
					if (!(TypeLinker.isAssignableFrom(paramType, parameterType))) {
						flag = false;
						break;
					}
				}
				if (flag)
					return method;
			}
		}
		for (Method method : clazz.getMethods()) {// 这里要处理Object...这种形式
			if (method.getName().equals(methodName))
				return method;
		}
		throw new RuntimeException("The method was not found!method:" + methodName);
	}

	public static IType visitMember(IType type, Type returnType) {
		// int --> Class<?>(int)
		// class [I --> Class<?>(int[])
		// class [Ljava.lang.String; --> Class<?>(java.lang.String[])
		// java.util.List<java.lang.String> --> parameterTypeImpl
		// E --> TypeVariableImpl
//		Class<?> clazz = ReflectUtils.getClass(type.getClassName());
//		if (returnType instanceof Class) {// 一部分类型可以直接转换
//			return (Class<?>) type;
//
//		} else if (returnType instanceof TypeVariable) {// 对象的其中一个泛型参数
//			String paramName = type.toString();// 泛型参数名称 E or K or V
//			int index = getTypeVariableIndex(clazz, paramName);
//			return type.getGenericTypes().get(index);
//
//		} else if (returnType instanceof ParameterizedType) {// 泛型
//			// 转换为泛型类型
//			ParameterizedType parameterizedType = (ParameterizedType) type;
//			// 类型
//			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
//			// 泛型集合
//			List<IType> genericTypes = new ArrayList<>();
//			// 获取该类型里面的泛型
//			for (Type actualType : parameterizedType.getActualTypeArguments()) {
//				// 递归
//				genericTypes.add(visitMember(type, actualType));
//			}
//			return new NativeType(clazz, genericTypes);
//
//		} else if (returnType instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
//			// 这里实在不知道放什么好,所以索性直接将这个不确定类型的class放进去了
//			return new NativeType(WildcardType.class);
//		}

		return null;

	}

	public static int getTypeVariableIndex(Class<?> clazz, String paramName) {
		TypeVariable<?>[] params = clazz.getTypeParameters();
		for (int i = 0; i < params.length; i++) {
			TypeVariable<?> param = params[i];
			if (param.toString().equals(paramName))
				return i;
		}
		return -1;
	}

}
