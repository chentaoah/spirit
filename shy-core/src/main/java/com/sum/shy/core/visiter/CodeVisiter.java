package com.sum.shy.core.visiter;

import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.deduce.InvokeVisiter;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.CodeType;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.core.visiter.api.Visiter;
import com.sum.shy.lib.StringUtils;

public class CodeVisiter implements Visiter {

	public Visiter nativeVisiter = new NativeVisiter();

	public IType visitField(IClass clazz, IType type, String fieldName) {

		if (type.isArray()) {
			if (Constants.$ARRAY_LENGTH.equals(fieldName))
				return new CodeType(clazz, Constants.INT_TYPE);
			throw new RuntimeException("Some functions of array are not supported yet!");

		} else {
			String className = type.getClassName();
			if (Context.get().contains(className)) {
				IClass typeClass = Context.get().findClass(className);
				if (StringUtils.isNotEmpty(fieldName)) {
					if (Constants.CLASS_KEYWORD.equals(fieldName)) {
						return new CodeType(typeClass, "Class<?>");
					}
//					if (typeClass.existField(fieldName)) {
//						IField field = typeClass.findField(fieldName);
//						return InvokeVisiter.visitMember(typeClass, field);
//
//					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
//						return visitField(typeClass, new CodeType(typeClass, typeClass.superName), fieldName);
//					}
				}
			} else {
				return nativeVisiter.visitField(clazz, type, fieldName);
			}
		}
		return null;
	}

	public IType visitMethod(IClass clazz, IType type, String methodName, List<IType> paramTypes) {

		if (type.isArray()) {
			if (Constants.$ARRAY_INDEX.equals(methodName))
				return new CodeType(clazz, type.getTypeName());
			throw new RuntimeException("Some functions of array are not supported yet!");

		} else {
			String className = type.getClassName();
			if (Context.get().contains(className)) {
				IClass typeClass = Context.get().findClass(className);
				if (StringUtils.isNotEmpty(methodName)) {
//					if (Constants.SUPER_KEYWORD.equals(methodName)) {
//						return new CodeType(typeClass, typeClass.superName);
//					}
//					if (Constants.THIS_KEYWORD.equals(methodName)) {
//						return new CodeType(typeClass, typeClass.typeName);
//					}
//					if (typeClass.existMethod(methodName, paramTypes)) {
//						IMethod method = typeClass.findMethod(methodName, paramTypes);
//						return InvokeVisiter.visitMember(typeClass, method);
//
//					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
//						return visitMethod(typeClass, new CodeType(typeClass, typeClass.superName), methodName,
//								paramTypes);
//					}
				}
			} else {
				return nativeVisiter.visitMethod(clazz, type, methodName, paramTypes);
			}
		}
		return null;
	}
}
