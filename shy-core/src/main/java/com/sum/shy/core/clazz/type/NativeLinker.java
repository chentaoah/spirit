package com.sum.shy.core.clazz.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.metadata.StaticType;
import com.sum.shy.core.utils.ReflectUtils;

public class NativeLinker {

	public static IType visitField(IType type, String fieldName) {
		try {
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			Field field = clazz.getField(fieldName);
			return convertNativeType(type, field.getGenericType());

		} catch (Exception e) {
			throw new RuntimeException("Failed to access field!fieldName:[" + fieldName + "]");
		}
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		try {
			Method method = findMethod(type, methodName, parameterTypes);
			return convertNativeType(type, method.getGenericReturnType());

		} catch (Exception e) {
			throw new RuntimeException("Failed to access method!methodName:[" + methodName + "]");
		}
	}

	private static Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		Class<?> clazz = ReflectUtils.getClass(type.getClassName());
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				boolean flag = true;
				int count = 0;
				for (Parameter parameter : method.getParameters()) {
					IType paramType = convertNativeType(type, parameter.getParameterizedType());
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

	public static IType convertNativeType(IType type, Type nativeType) {

		if (nativeType instanceof Class) {// 一部分类型可以直接转换
			return TypeFactory.createNativeType((Class<?>) nativeType, null);

		} else if (nativeType instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
			return StaticType.WILDCARD_TYPE;

		} else if (nativeType instanceof TypeVariable) {// 泛型参数 E or K or V
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			int index = getTypeVariableIndex(clazz, nativeType.toString());// 获取这个泛型名称在类中的index
			return type.getGenericTypes().get(index);

		} else if (nativeType instanceof ParameterizedType) {// 泛型 List<E>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> clazz = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			for (Type actualType : parameterizedType.getActualTypeArguments())
				genericTypes.add(convertNativeType(type, actualType));
			return TypeFactory.createNativeType(clazz, genericTypes);

		}
		return null;
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
