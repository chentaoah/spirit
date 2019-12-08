package com.sum.shy.core.analyzer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.NativeType;

public class NativeLinker {

	public static Type getReturnType(CtClass clazz, Type type, List<String> members, String methodName,
			List<Type> parameterTypes) {
		// 类名
		NativeType nativeType = type instanceof CodeType ? new NativeType(type) : (NativeType) type;
		try {
			if (members != null && members.size() > 0) {
				String member = members.remove(0);// 获取第一个属性
				Field field = nativeType.clazz.getField(member);
				Type returnType = visitElement(nativeType, field.getGenericType());
				if (members.size() > 0 || methodName != null)
					returnType = getReturnType(clazz, returnType, members, methodName, parameterTypes);
				return returnType;

			} else if (methodName != null) {
				Method method = nativeType.findMethod(methodName, parameterTypes);
				return visitElement(nativeType, method.getGenericReturnType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Type visitElement(NativeType nativeType, java.lang.reflect.Type type) {
		// int --> Class<?>(int)
		// class [I --> Class<?>(int[])
		// class [Ljava.lang.String; --> Class<?>(java.lang.String[])
		// java.util.List<java.lang.String> --> parameterTypeImpl
		// E --> TypeVariableImpl

		if (type instanceof Class<?>) {// 一部分类型可以直接转换
			return new NativeType((Class<?>) type);

		} else if (type instanceof TypeVariable<?>) {// 对象的其中一个泛型参数
			String paramName = type.toString();// 泛型参数名称 E or K or V
			int index = getTypeVariableIndex(nativeType.clazz, paramName);
			return nativeType.genericTypes.get(index);

		} else if (type instanceof ParameterizedType) {// 泛型
			// 转换为泛型类型
			ParameterizedType parameterizedType = (ParameterizedType) type;
			// 类型
			Class<?> clazz = (Class<?>) parameterizedType.getRawType();
			// 泛型集合
			List<Type> genericTypes = new ArrayList<>();
			// 获取该类型里面的泛型
			for (java.lang.reflect.Type actualType : parameterizedType.getActualTypeArguments()) {
				// 递归
				genericTypes.add(visitElement(nativeType, actualType));
			}
			return new NativeType(clazz, genericTypes);
		}

		return null;

	}

	public static int getTypeVariableIndex(Class<?> clazz, String paramName) {
		TypeVariable<?>[] params = clazz.getTypeParameters();
		for (int i = 0; i < params.length; i++) {
			TypeVariable<?> param = params[i];
			if (param.toString().equals(paramName)) {
				return i;
			}
		}
		return -1;
	}

}
