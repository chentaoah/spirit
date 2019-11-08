package com.sum.shy.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.entity.NativeType;
import com.sum.shy.core.entity.Type;

public class ReflectUtils {

	/**
	 * 不支持java重载 不支持java泛型
	 * 
	 * @param className
	 * @param memberVarNames
	 * @param methodName
	 * @return
	 */
	public static Type getReturnType(String className, List<String> memberVarNames, String methodName) {
		try {
			Class<?> clazz = Class.forName(className);
			Class<?> fieldType = clazz;
			for (String memberVarName : memberVarNames) {
				Field field = fieldType.getField(memberVarName);
				fieldType = field.getType();
			}
			// 遍历方法
			for (Method method : fieldType.getMethods()) {
				if (method.getName().equals(methodName)) {
					return new Type(getNativeType(method));
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Type getFieldType(String className, List<String> memberVarNames) {
		try {
			Class<?> clazz = Class.forName(className);
			Class<?> fieldType = clazz;
			for (String memberVarName : memberVarNames) {
				Field field = fieldType.getField(memberVarName);
				fieldType = field.getType();
			}
			return new Type(fieldType.getSimpleName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取java所描述的类型信息
	 * 
	 * @param method
	 * @return
	 */
	public static NativeType getNativeType(Method method) {
		// 获取返回类型
		Class<?> clazz = method.getReturnType();
		// 获取泛型参数名
		TypeVariable<?>[] params = clazz.getTypeParameters();
		// 获取返回值,当前真正的泛型参数
		List<Class<?>> genericTypes = getNativeGenericTypes(method);
		Map<String, Class<?>> map = new LinkedHashMap<>();
		for (int i = 0; i < params.length; i++) {
			map.put(params[i].getName(), genericTypes.get(i));
		}
		return new NativeType(clazz, map);
	}

	/**
	 * 获取java描述的泛型信息
	 * 
	 * @param method
	 * @return
	 */
	public static List<Class<?>> getNativeGenericTypes(Method method) {
		List<Class<?>> genericTypes = new ArrayList<>();
		java.lang.reflect.Type genericReturnType = method.getGenericReturnType();
		if (genericReturnType instanceof ParameterizedType) {
			java.lang.reflect.Type[] actualTypeArguments = ((ParameterizedType) genericReturnType)
					.getActualTypeArguments();
			for (java.lang.reflect.Type actualTypeArgument : actualTypeArguments) {
				if (actualTypeArgument instanceof Class) {
					genericTypes.add((Class<?>) actualTypeArgument);
				}
			}
		}
		return genericTypes;
	}

}
