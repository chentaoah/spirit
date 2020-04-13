package com.sum.shy.core;

import java.util.List;

import com.sum.shy.core.clazz.IType;

public class TypeLinker {

	public static IType visitField(IType type, String fieldName) {
		return null;
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		return null;
	}

	public static boolean isAssignableFrom(IType father, IType type) {
		return false;
	}

}
