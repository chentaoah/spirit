package com.sum.shy.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.NativeType;

public class ReflectUtils {

	public static NativeType getNativeType(Clazz clazz, String type) {

		// 基本类型
		Class<?> class1 = getClassByStr(type);
		if (class1 != null)
			return new NativeType(class1);

		// 泛型
		NativeType nativeType = getNativeTypeByStr(type);
		if (nativeType != null)
			return nativeType;

		// 简单类型
		String className = clazz.findImport(type);
		return new NativeType(ReflectUtils.getClass(className));
	}

	private static Class<?> getClassByStr(String type) {
		switch (type) {
		case "boolean":
			return boolean.class;
		case "int":
			return int.class;
		case "long":
			return long.class;
		case "double":
			return double.class;
		case "Boolean":
			return Boolean.class;
		case "Integer":
			return Integer.class;
		case "Long":
			return Long.class;
		case "Double":
			return Double.class;
		case "Object":
			return Object.class;
		case "String":
			return String.class;
		case "boolean[]":
			return boolean[].class;
		case "int[]":
			return int[].class;
		case "long[]":
			return long[].class;
		case "double[]":
			return double[].class;
		case "Boolean[]":
			return Boolean[].class;
		case "Integer[]":
			return Integer[].class;
		case "Long[]":
			return Long[].class;
		case "Double[]":
			return Double[].class;
		case "Object[]":
			return Object[].class;
		case "String[]":
			return String[].class;
		default:
			return null;
		}

	}

	private static NativeType getNativeTypeByStr(String type) {
		if (type.contains("<") && type.contains(">")) {

		}
		return null;
	}

	private static NativeType getNativeType(Clazz clazz, List<String> list) {
		String first = list.get(0);
		Class<?> class1 = getClass(clazz.findImport(first));
		// 获取泛型参数名
		TypeVariable<?>[] params = class1.getTypeParameters();
		// 如果是带泛型的
		Map<String, NativeType> genericTypes = new HashMap<>();
		if (params.length > 0 && list.size() > 1) {
			Class<?> genericClazz = null;
			for (int i = 1; i < list.size(); i++) {
				String str = list.get(i);
				String className = clazz.findImport(str);
				genericClazz = getClass(className);
				genericTypes.put(params[i - 1].toString(), new NativeType(genericClazz));
			}
		}
		return new NativeType(class1, genericTypes);
	}

	public static NativeType getReturnType(String className) {
		return new NativeType(getClass(className));
	}

	public static NativeType getReturnType(String className, String methodName) {

		Class<?> clazz = getClass(className);
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				return getNativeType(null, method.getGenericReturnType());
			}
		}

		return null;
	}

	public static NativeType getReturnType(NativeType nativeType, List<String> varNames, String methodName) {

		nativeType = getFieldType(nativeType, varNames);
		// 不支持重载
		for (Method method : nativeType.clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				return getNativeType(nativeType, method.getGenericReturnType());
			}
		}

		return null;
	}

	public static NativeType getFieldType(String className, List<String> varNames) {
		NativeType nativeType = new NativeType(getClass(className));
		return getFieldType(nativeType, varNames);
	}

	public static NativeType getFieldType(NativeType nativeType, List<String> varNames) {
		try {
			for (String varName : varNames) {
				Field field = nativeType.clazz.getField(varName);
				nativeType = getNativeType(nativeType, field.getGenericType());
			}
			return nativeType;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取类型
	 * 
	 * @param nativeType
	 * @param genericType
	 * @return
	 */
	public static NativeType getNativeType(NativeType nativeType, Type genericType) {
		String text = genericType.toString();
		// 这里也仅仅是支持了一级而已
		List<String> list = Splitter.on(CharMatcher.anyOf("<,>")).omitEmptyStrings().trimResults().splitToList(text);
		Class<?> clazz = null;
		String first = list.get(0);
		// 基本类型
//		if (isPrimitive(first)) {
//			return new NativeType(getPrimitive(first));
//		}
		// 如果是一个简单类型
		if (first.startsWith("class ")) {
			clazz = getClass(first.substring(first.indexOf(" ") + 1));
			return new NativeType(clazz);
		}
		// 如果整个就是个泛型
		if (!first.contains(".")) {
			return nativeType == null ? new NativeType(Object.class) : nativeType.genericTypes.get(first);
		}
		// 创建一个新的类型，但是这个类型里面包含了上个类型的泛型
		clazz = getClass(first);
		Map<String, NativeType> genericTypes = new HashMap<>();
		// 获取泛型参数名
		TypeVariable<?>[] params = clazz.getTypeParameters();
		// 如果是带泛型的
		if (params.length > 0 && list.size() > 1) {
			for (int i = 1; i < list.size(); i++) {
				String str = list.get(i);
				// 如果是一个泛型
				if (!str.contains(".")) {
					genericTypes.put(params[i - 1].toString(),
							nativeType == null ? new NativeType(Object.class) : nativeType.genericTypes.get(str));
				} else {
					genericTypes.put(params[i - 1].toString(), new NativeType(getClass(str)));
				}

			}

		}

		return new NativeType(clazz, genericTypes);

	}

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isPrimitive(String className) {
		// TODO Auto-generated method stub
		return false;
	}

}
