package com.sum.shy.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.NativeType;

public class ReflectUtils {

	public static Type getReturnType(CtClass clazz, Type type, List<String> members, String methodName) {
		// 类名
		NativeType nativeType = type instanceof CodeType ? new NativeType(type) : (NativeType) type;
		try {
			if (members != null && members.size() > 0) {
				String member = members.remove(0);// 获取第一个属性
				Field field = nativeType.clazz.getField(member);
				Type returnType = visitElement(nativeType, field.getGenericType());
				if (members.size() > 0 || methodName != null)
					returnType = getReturnType(clazz, returnType, members, methodName);
				return returnType;

			} else if (methodName != null) {
				Method method = nativeType.findMethod(methodName);
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
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Class<?> clazz = (Class<?>) parameterizedType.getRawType();
			List<NativeType> genericTypes = new ArrayList<>();
			for (java.lang.reflect.Type actualType : parameterizedType.getActualTypeArguments()) {
				if (actualType instanceof Class<?>) {
					genericTypes.add(new NativeType((Class<?>) actualType));

				} else if (actualType instanceof TypeVariable<?>) {
					String paramName = actualType.toString();// 泛型参数名称 E or K or V
					int index = getTypeVariableIndex(nativeType.clazz, paramName);
					genericTypes.add(nativeType.genericTypes.get(index));

				}
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

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isArray(String simpleName) {
		return simpleName.endsWith("[]");
	}

	public static String getTypeName(String simpleName) {
		return isArray(simpleName) ? simpleName.substring(0, simpleName.indexOf("[")) : simpleName;
	}

	public static String getClassName(String className) {
		return className.startsWith("[L") && className.endsWith(";") ? className.substring(2, className.length() - 1)
				: className;
	}

	public static String getCommonType(String simpleName) {

		switch (simpleName) {
		// 空类型
		case "void":
			return void.class.getName();
		// 基本类型
		case "boolean":
			return boolean.class.getName();
		case "char":
			return char.class.getName();
		case "short":
			return short.class.getName();
		case "int":
			return int.class.getName();
		case "long":
			return long.class.getName();
		case "float":
			return float.class.getName();
		case "double":
			return double.class.getName();
		case "byte":
			return byte.class.getName();
		// 基本类型数组
		case "boolean[]":
			return boolean[].class.getName();
		case "char[]":
			return char[].class.getName();
		case "short[]":
			return short[].class.getName();
		case "int[]":
			return int[].class.getName();
		case "long[]":
			return long[].class.getName();
		case "float[]":
			return float[].class.getName();
		case "double[]":
			return double[].class.getName();
		case "byte[]":
			return byte[].class.getName();
		// 包装类
		case "Boolean":
			return Boolean.class.getName();
		case "Character":
			return Character.class.getName();
		case "Short":
			return Short.class.getName();
		case "Integer":
			return Integer.class.getName();
		case "Long":
			return Long.class.getName();
		case "Float":
			return Float.class.getName();
		case "Double":
			return Double.class.getName();
		case "Byte":
			return Byte.class.getName();
		// 包装类数组
		case "Boolean[]":
			return Boolean[].class.getName();
		case "Character[]":
			return Character[].class.getName();
		case "Short[]":
			return Short[].class.getName();
		case "Integer[]":
			return Integer[].class.getName();
		case "Long[]":
			return Long[].class.getName();
		case "Float[]":
			return Float[].class.getName();
		case "Double[]":
			return Double[].class.getName();
		case "Byte[]":
			return Byte[].class.getName();
		// 类
		case "Object":
			return Object.class.getName();
		case "String":
			return String.class.getName();
		case "Exception":
			return Exception.class.getName();
		// 类数组
		case "Object[]":
			return Object[].class.getName();
		case "String[]":
			return String[].class.getName();

		default:
			return null;
		}
	}

	public static String getCollectionType(String typeName) {
		switch (typeName) {
		case "List":
			return List.class.getName();
		case "Map":
			return Map.class.getName();
		default:
			return null;
		}

	}

}
