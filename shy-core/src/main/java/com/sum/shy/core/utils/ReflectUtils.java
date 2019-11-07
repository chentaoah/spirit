package com.sum.shy.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

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
			for (Method method : fieldType.getMethods()) {
				if (method.getName().equals(methodName)) {
					String returnType = method.getReturnType().getSimpleName();
					return new Type(returnType);
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

}
