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
import com.sum.shy.core.utils.ReflectUtils;
import com.sum.shy.lib.StringUtils;

public class NativeLinker {

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
					IType paramType = NativeLinker.visitMember(type, parameter.getParameterizedType());
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

	public static IType visitMember(IType type, Type memberType) {

		if (memberType instanceof Class) {// 一部分类型可以直接转换
			return createNativeType((Class<?>) memberType, null);

		} else if (memberType instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
			return createNativeType(WildcardType.class, null);// 这里实在不知道放什么好,所以索性直接将这个不确定类型的class放进去了

		} else if (memberType instanceof TypeVariable) {// 泛型参数 E or K or V
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			int index = getTypeVariableIndex(clazz, memberType.toString());// 获取这个泛型名称在类中的index
			return type.getGenericTypes().get(index);

		} else if (memberType instanceof ParameterizedType) {// 泛型 List<E>
			ParameterizedType parameterizedType = (ParameterizedType) memberType;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			for (Type actualType : parameterizedType.getActualTypeArguments())
				genericTypes.add(visitMember(type, actualType));
			return createNativeType(rawType, genericTypes);
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

	public static IType createNativeType(Class<?> clazz, List<IType> genericTypes) {
		return null;
	}

}
