package com.sum.shy.core.clazz.type;

import java.util.List;

import com.sum.shy.core.MemberVisiter;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.Context;

public class CodeLinker {

	public static IType visitField(IType type, String fieldName) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existField(fieldName)) {
			IField field = clazz.getField(fieldName);
			return MemberVisiter.visitMember(clazz, field);

		} else {
			return AdaptiveLinker.visitField(clazz.getSuperType(), fieldName);
		}
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existMethod(methodName, parameterTypes)) {
			IMethod method = clazz.getMethod(methodName, parameterTypes);
			return MemberVisiter.visitMember(clazz, method);

		} else {
			return AdaptiveLinker.visitMethod(clazz.getSuperType(), methodName, parameterTypes);
		}

	}
}
