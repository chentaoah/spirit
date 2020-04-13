package com.sum.shy.core;

import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.lib.StringUtils;

public class TypeLinker {

	public static IType visitField(IType type, String fieldName) {
		return !type.isNative() ? visitCodeField(type, fieldName) : visitNativeField(type, fieldName);
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		return !type.isNative() ? visitCodeMethod(type, methodName, parameterTypes)
				: visitNativeMethod(type, methodName, parameterTypes);
	}

	private static IType visitCodeField(IType type, String fieldName) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (StringUtils.isNotEmpty(fieldName)) {
			if (Constants.CLASS_KEYWORD.equals(fieldName))
				return TypeFactory.create(type.getDeclarer(), "Class<?>");

			if (clazz.existField(fieldName)) {
				IField field = clazz.getField(fieldName);
				return MemberVisiter.visitMember(clazz, field);

			} else if (StringUtils.isNotEmpty(clazz.getSuperName())) {
				return visitField(TypeFactory.create(clazz, clazz.getSuperName()), fieldName);
			}
		}
		return null;
	}

	private static IType visitCodeMethod(IType type, String methodName, List<IType> parameterTypes) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (StringUtils.isNotEmpty(methodName)) {
			if (Constants.SUPER_KEYWORD.equals(methodName))
				return TypeFactory.create(clazz, clazz.getSuperName());

			if (Constants.THIS_KEYWORD.equals(methodName))
				return TypeFactory.create(clazz, clazz.getTypeName());

			if (clazz.existMethod(methodName, parameterTypes)) {
				IMethod method = clazz.getMethod(methodName, parameterTypes);
				return MemberVisiter.visitMember(clazz, method);

			} else if (StringUtils.isNotEmpty(clazz.getSuperName())) {
				return visitMethod(TypeFactory.create(clazz, clazz.getSuperName()), methodName, parameterTypes);
			}
		}
		return null;
	}

	private static IType visitNativeField(IType type, String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	private static IType visitNativeMethod(IType type, String methodName, List<IType> parameterTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean isAssignableFrom(IType father, IType type) {
		return false;
	}

}
