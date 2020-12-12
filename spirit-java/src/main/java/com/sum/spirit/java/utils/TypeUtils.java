package com.sum.spirit.java.utils;

import java.util.List;
import java.util.Map;

import com.sum.spirit.pojo.common.IType;

public class TypeUtils {

	public static boolean isVoid(IType type) {
		return void.class.getName().equals(type.getClassName());
	}

	public static boolean isObject(IType type) {
		return Object.class.getName().equals(type.getClassName());
	}

	public static boolean isString(IType type) {
		return String.class.getName().equals(type.getClassName());
	}

	public static boolean isList(IType type) {
		return List.class.getName().equals(type.getClassName());
	}

	public static boolean isMap(IType type) {
		return Map.class.getName().equals(type.getClassName());
	}
}
