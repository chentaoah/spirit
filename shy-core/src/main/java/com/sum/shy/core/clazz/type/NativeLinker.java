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
		try {
			if (StringUtils.isNotEmpty(fieldName)) {
				Class<?> clazz = ReflectUtils.getClass(type.getClassName());
				Field field = clazz.getField(fieldName);
				return convertNativeType(type, field.getGenericType());
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
				return convertNativeType(type, method.getGenericReturnType());
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
			return createNativeType(type, (Class<?>) nativeType, null);

		} else if (nativeType instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
			return createNativeType(type, WildcardType.class, null);// 这里实在不知道放什么好,所以索性直接将这个不确定类型的class放进去了

		} else if (nativeType instanceof TypeVariable) {// 泛型参数 E or K or V
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			int index = getTypeVariableIndex(clazz, nativeType.toString());// 获取这个泛型名称在类中的index
			return type.getGenericTypes().get(index);

		} else if (nativeType instanceof ParameterizedType) {// 泛型 List<E>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			for (Type actualType : parameterizedType.getActualTypeArguments())
				genericTypes.add(convertNativeType(type, actualType));
			return createNativeType(type, rawType, genericTypes);
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

	public static IType createNativeType(IType type, Class<?> clazz, List<IType> genericTypes) {
		IType nativeType = new IType();
		if (clazz == WildcardType.class) {
			nativeType.setClassName(WildcardType.class.getName());
			nativeType.setSimpleName("?");
			nativeType.setTypeName("?");
			nativeType.setPrimitive(false);
			nativeType.setArray(false);
			nativeType.setGenericType(false);
			nativeType.setGenericTypes(new ArrayList<>());
			nativeType.setWildcard(true);
			nativeType.setDeclarer(type.getDeclarer());
			nativeType.setNative(true);

		} else {
			nativeType.setClassName(clazz.getName());
			nativeType.setSimpleName(clazz.getSimpleName());
			nativeType.setTypeName(clazz.getTypeName());
			nativeType.setPrimitive(clazz.isPrimitive());
			nativeType.setArray(clazz.isArray());
			nativeType.setGenericType(genericTypes != null && genericTypes.size() > 0);
			nativeType.setGenericTypes(genericTypes);
			nativeType.setWildcard(false);
			nativeType.setDeclarer(type.getDeclarer());
			nativeType.setNative(true);
		}
		return nativeType;
	}

}
