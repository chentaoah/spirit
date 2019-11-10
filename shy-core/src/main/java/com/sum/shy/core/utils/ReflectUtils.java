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
		if (isPrimitive(type)) {
			return new NativeType(getPrimitive(type));
		}
		// 复杂类型
		String className = clazz.findImport(type);
		return new NativeType(ReflectUtils.getClass(className));
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

	public static NativeType getReturnType(NativeType nativeType, List<String> memberVarNames, String methodName) {

		nativeType = getFieldType(nativeType, memberVarNames);
		// 不支持重载
		for (Method method : nativeType.clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				return getNativeType(nativeType, method.getGenericReturnType());
			}
		}

		return null;
	}

	public static NativeType getFieldType(NativeType nativeType, List<String> memberVarNames) {
		try {
			for (String memberVarName : memberVarNames) {
				Field field = nativeType.clazz.getField(memberVarName);
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
		if (isPrimitive(first)) {
			return new NativeType(getPrimitive(first));
		}
		// 如果是一个简单类型
		if (first.startsWith("class ")) {
			clazz = getClass(first.substring(first.indexOf(" ") + 1));
			return new NativeType(clazz);
		}
		// 如果整个就是个泛型
		if (!first.contains(".")) {
			clazz = nativeType == null ? Object.class : nativeType.genericTypes.get(first).clazz;
			return new NativeType(clazz);
		}
		clazz = getClass(first);
		Map<String, NativeType> genericTypes = new HashMap<>();
		// 获取泛型参数名
		TypeVariable<?>[] params = clazz.getTypeParameters();
		// 如果是带泛型的
		if (params.length > 0 && list.size() > 1) {
			Class<?> genericClazz = null;
			for (int i = 1; i < list.size(); i++) {
				String str = list.get(i);
				// 如果是一个泛型
				if (!str.contains(".")) {
					genericClazz = nativeType == null ? Object.class : nativeType.genericTypes.get(str).clazz;
				} else {
					genericClazz = getClass(str);
				}
				genericTypes.put(params[i - 1].toString(), new NativeType(genericClazz));

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

	public static boolean isPrimitive(String type) {
		return "boolean".equals(type) || "int".equals(type) || "double".equals(type);
	}

	public static Class<?> getPrimitive(String type) {
		switch (type) {
		case "boolean":
			return boolean.class;
		case "int":
			return int.class;
		case "double":
			return double.class;
		default:
			return null;
		}
	}

}
