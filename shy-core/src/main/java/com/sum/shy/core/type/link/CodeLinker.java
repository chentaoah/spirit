package com.sum.shy.core.type.link;

import java.util.List;

import com.sum.shy.core.MemberVisiter;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.IType;
import com.sum.shy.core.type.TypeFactory;
import com.sum.shy.lib.StringUtils;

public class CodeLinker {

	public static IType visitField(IType type, String fieldName) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (StringUtils.isNotEmpty(fieldName)) {
			if (Constants.CLASS_KEYWORD.equals(fieldName))
				return TypeFactory.create(type.getDeclarer(), "Class<?>");

			if (clazz.existField(fieldName)) {
				IField field = clazz.getField(fieldName);
				return MemberVisiter.visitMember(clazz, field);

			} else if (StringUtils.isNotEmpty(clazz.getSuperName())) {
				return TypeLinker.visitField(TypeFactory.create(clazz, clazz.getSuperName()), fieldName);
			}
		}
		return null;
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
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
				return TypeLinker.visitMethod(TypeFactory.create(clazz, clazz.getSuperName()), methodName,
						parameterTypes);
			}
		}
		return null;
	}

}
