package com.sum.shy.core.clazz.type;

import java.util.List;

import com.sum.shy.core.MemberVisiter;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.lib.StringUtils;

public class CodeLinker {

	public static IType visitField(IType type, String fieldName) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existField(fieldName)) {
			IField field = clazz.getField(fieldName);
			return MemberVisiter.visitMember(clazz, field);

		} else if (StringUtils.isNotEmpty(clazz.getSuperName())) {
			return TypeLinker.visitField(TypeFactory.create(clazz.getSuperName()), fieldName);
		}
		return null;
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existMethod(methodName, parameterTypes)) {
			IMethod method = clazz.getMethod(methodName, parameterTypes);
			return MemberVisiter.visitMember(clazz, method);

		} else if (StringUtils.isNotEmpty(clazz.getSuperName())) {
			return TypeLinker.visitMethod(TypeFactory.create(clazz.getSuperName()), methodName, parameterTypes);
		}
		return null;
	}
}
