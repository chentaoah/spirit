package com.sum.shy.core.clazz.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.StaticType;
import com.sum.shy.core.utils.ReflectUtils;

public class NativeLinker {

	public static IType visitField(IType type, String fieldName) {
		try {
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			Field field = clazz.getField(fieldName);
			return convertNativeType(type, null, null, field.getGenericType());

		} catch (Exception e) {// 大家都是Object的子类后，这个方法会被高频调用
			// ignore
		}
		return null;
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		try {
			Map<String, IType> methodGenericTypes = new HashMap<>();
			Method method = findMethod(type, methodName, parameterTypes, methodGenericTypes);
			return convertNativeType(type, methodGenericTypes, null, method.getGenericReturnType());

		} catch (Exception e) {// 大家都是Object的子类后，这个方法会被高频调用
			// ignore
		}
		return null;
	}

	public static Method findMethod(IType type, String methodName, List<IType> parameterTypes,
			Map<String, IType> methodGenericTypes) {

		Class<?> clazz = ReflectUtils.getClass(type.getClassName());
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				boolean flag = true;
				int count = 0;
				for (Parameter parameter : method.getParameters()) {
					IType parameterType = parameterTypes.get(count++);
					IType nativeParameterType = convertNativeType(type, methodGenericTypes, parameterType,
							parameter.getParameterizedType());
					if (!(nativeParameterType.isMatch(parameterType))) {
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

	public static IType convertNativeType(IType type, Map<String, IType> methodGenericTypes, IType referenceTyep,
			Type nativeType) {

		if (nativeType instanceof Class) {// 一部分类型可以直接转换
			return TypeFactory.create((Class<?>) nativeType);

		} else if (nativeType instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
			return StaticType.WILDCARD_TYPE;

		} else if (nativeType instanceof TypeVariable) {// 泛型参数 E or K or V
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			int index = getTypeVariableIndex(clazz, nativeType.toString());// 获取这个泛型名称在类中的index
			if (index >= 0) {
				return type.getGenericTypes().get(index);
			} else {
				if (methodGenericTypes != null && methodGenericTypes.containsKey(nativeType.toString())) {
					return methodGenericTypes.get(nativeType.toString());
				} else {
					methodGenericTypes.put(nativeType.toString(), referenceTyep);
					return referenceTyep;
				}
			}
		} else if (nativeType instanceof ParameterizedType) {// 泛型 List<E>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> clazz = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			int index = 0;
			for (Type actualType : parameterizedType.getActualTypeArguments()) {
				referenceTyep = referenceTyep != null ? referenceTyep.getGenericTypes().get(index++) : null;
				genericTypes.add(convertNativeType(type, methodGenericTypes, referenceTyep, actualType));
			}
			return TypeFactory.create(clazz, genericTypes);
		}
		throw new RuntimeException("Convert native type failed!");
	}

	public static int getTypeVariableIndex(Class<?> clazz, String typeVariableName) {
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		for (int i = 0; i < typeVariables.length; i++) {
			TypeVariable<?> typeVariable = typeVariables[i];
			if (typeVariable.toString().equals(typeVariableName))
				return i;
		}
		return -1;
	}

}
