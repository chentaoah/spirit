package com.sum.shy.visiter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.type.CodeType;
import com.sum.shy.type.NativeType;
import com.sum.shy.type.api.IType;
import com.sum.shy.visiter.api.Visiter;

public class NativeVisiter implements Visiter {

	public IType visitField(IClass iClass, IType type, String fieldName) {
		NativeType nativeType = type instanceof CodeType ? new NativeType(iClass, type) : (NativeType) type;
		try {
			if (StringUtils.isNotEmpty(fieldName)) {
				Field field = nativeType.clazz.getField(fieldName);
				return visitMember(iClass, nativeType, field.getGenericType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public IType visitMethod(IClass iClass, IType type, String methodName, List<IType> paramTypes) {
		NativeType nativeType = type instanceof CodeType ? new NativeType(iClass, type) : (NativeType) type;
		try {
			if (StringUtils.isNotEmpty(methodName)) {
				Method method = nativeType.findMethod(methodName, paramTypes);
				return visitMember(iClass, nativeType, method.getGenericReturnType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static IType visitMember(IClass iClass, NativeType nativeType, Type type) {
		// int --> Class<?>(int)
		// class [I --> Class<?>(int[])
		// class [Ljava.lang.String; --> Class<?>(java.lang.String[])
		// java.util.List<java.lang.String> --> parameterTypeImpl
		// E --> TypeVariableImpl

		if (type instanceof Class) {// 一部分类型可以直接转换
			return new NativeType(iClass, (Class<?>) type);

		} else if (type instanceof TypeVariable) {// 对象的其中一个泛型参数
			String paramName = type.toString();// 泛型参数名称 E or K or V
			int index = getTypeVariableIndex(nativeType.clazz, paramName);
			return nativeType.genericTypes.get(index);

		} else if (type instanceof ParameterizedType) {// 泛型
			// 转换为泛型类型
			ParameterizedType parameterizedType = (ParameterizedType) type;
			// 类型
			Class<?> clazz = (Class<?>) parameterizedType.getRawType();
			// 泛型集合
			List<IType> genericTypes = new ArrayList<>();
			// 获取该类型里面的泛型
			for (Type actualType : parameterizedType.getActualTypeArguments()) {
				// 递归
				genericTypes.add(visitMember(iClass, nativeType, actualType));
			}
			return new NativeType(iClass, clazz, genericTypes);

		} else if (type instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
			// 这里实在不知道放什么好,所以索性直接将这个不确定类型的class放进去了
			return new NativeType(iClass, WildcardType.class);
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
