package com.sum.shy.deducer;

import java.util.List;

import com.sum.shy.api.service.ElementMemberVisiter;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.IType;
import com.sum.shy.common.Context;

public class CodeLinker {

	public static IType visitField(IType type, String fieldName) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existField(fieldName)) {
			IField field = clazz.getField(fieldName);
			return ElementMemberVisiter.visitMember(clazz, field);

		} else {
			return AdaptiveLinker.visitField(clazz.getSuperType(), fieldName);
		}
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existMethod(methodName, parameterTypes)) {
			IMethod method = clazz.getMethod(methodName, parameterTypes);
			return ElementMemberVisiter.visitMember(clazz, method);

		} else {
			return AdaptiveLinker.visitMethod(clazz.getSuperType(), methodName, parameterTypes);
		}

	}
}
