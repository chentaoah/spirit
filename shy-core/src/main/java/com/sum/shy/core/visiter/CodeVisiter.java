package com.sum.shy.core.visiter;

import java.util.List;

import com.sum.shy.core.MemberVisiter;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.visiter.api.Visiter;
import com.sum.shy.lib.StringUtils;

public class CodeVisiter implements Visiter {

	public IType visitField(IClass clazz, IType type, String fieldName) {
		String className = type.getClassName();
		IClass typeClass = Context.get().findClass(className);
		if (StringUtils.isNotEmpty(fieldName)) {
			if (Constants.CLASS_KEYWORD.equals(fieldName)) {
				return new CodeType(typeClass, "Class<?>");
			}
			if (typeClass.existField(fieldName)) {
				IField field = typeClass.getField(fieldName);
				return MemberVisiter.visitMember(typeClass, field);

			} else if (StringUtils.isNotEmpty(typeClass.getSuperName())) {
				return visitField(typeClass, new CodeType(typeClass, typeClass.getSuperName()), fieldName);
			}
		}
		return null;
	}

	public IType visitMethod(IClass clazz, IType type, String methodName, List<IType> parameterTypes) {
		String className = type.getClassName();
		IClass typeClass = Context.get().findClass(className);
		if (StringUtils.isNotEmpty(methodName)) {
			if (Constants.SUPER_KEYWORD.equals(methodName)) {
				return new CodeType(typeClass, typeClass.getSuperName());
			}
			if (Constants.THIS_KEYWORD.equals(methodName)) {
				return new CodeType(typeClass, typeClass.getTypeName());
			}
			if (typeClass.existMethod(methodName, parameterTypes)) {
				IMethod method = typeClass.getMethod(methodName, parameterTypes);
				return MemberVisiter.visitMember(typeClass, method);

			} else if (StringUtils.isNotEmpty(typeClass.getSuperName())) {
				return visitMethod(typeClass, new CodeType(typeClass, typeClass.getSuperName()), methodName,
						parameterTypes);
			}
		}
		return null;
	}
}
