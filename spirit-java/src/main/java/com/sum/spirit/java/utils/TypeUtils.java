package com.sum.spirit.java.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.type.IType;

public class TypeUtils {

	public static String build(IClass clazz, IType type) {

		if (type.isWildcard()) {
			return "?";
		}

		if (type.isTypeVariable())
		 {
			return type.getGenericName();// T K
		}

		String finalName = clazz.addImport(type.getTargetName()) ? type.getSimpleName() : type.getTypeName();

		if (type.isGenericType()) {// 泛型
			List<String> strs = new ArrayList<>();
			for (IType genericType : type.getGenericTypes()) {
				strs.add(build(clazz, genericType));
			}
			return finalName + "<" + Joiner.on(", ").join(strs) + ">";
		}

		return finalName;

	}

	public static boolean isVoid(IType type) {
		return void.class.getName().equals(type.getClassName());
	}

	public static boolean isObj(IType type) {
		return Object.class.getName().equals(type.getClassName());
	}

	public static boolean isStr(IType type) {
		return String.class.getName().equals(type.getClassName());
	}

	public static boolean isList(IType type) {
		return List.class.getName().equals(type.getClassName());
	}

	public static boolean isMap(IType type) {
		return Map.class.getName().equals(type.getClassName());
	}
}
