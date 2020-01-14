package com.sum.shy.visiter;

import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.core.deduce.InvokeVisiter;
import com.sum.shy.core.entity.Context;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.type.CodeType;
import com.sum.shy.type.api.Type;

public class CodeVisiter {

	public static Type visitField(IClass clazz, Type type, String fieldName) {
		if (type.isArray()) {
			if ("length".equals(fieldName))
				return new CodeType(clazz, "int");
			throw new RuntimeException("Some functions of array are not supported yet!");
		} else {
			String className = type.getClassName();
			if (Context.get().contains(className)) {
				IClass typeClass = Context.get().findClass(className);
				if (StringUtils.isNotEmpty(fieldName)) {
					if ("class".equals(fieldName)) {
						return new CodeType(typeClass, "Class<?>");
					}
					if (typeClass.existField(fieldName)) {
						IField field = typeClass.findField(fieldName);
						return InvokeVisiter.visitMember(typeClass, field);

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
						return visitField(typeClass, new CodeType(typeClass, typeClass.superName), fieldName);
					}
				}
			} else {
				return NativeVisiter.visitField(clazz, type, fieldName);
			}
		}
		return null;
	}

	public static Type visitMethod(IClass clazz, Type type, String methodName, List<Type> parameterTypes) {
		if (type.isArray()) {
			if ("$array_index".equals(methodName))
				return new CodeType(clazz, type.getTypeName());
			throw new RuntimeException("Some functions of array are not supported yet!");
		} else {
			String className = type.getClassName();
			if (Context.get().contains(className)) {
				IClass typeClass = Context.get().findClass(className);
				if (StringUtils.isNotEmpty(methodName)) {
					if ("super".equals(methodName)) {
						return new CodeType(typeClass, typeClass.superName);
					}
					if ("this".equals(methodName)) {
						return new CodeType(typeClass, typeClass.typeName);
					}
					if (typeClass.existMethod(methodName, parameterTypes)) {
						IMethod method = typeClass.findMethod(methodName, parameterTypes);
						return InvokeVisiter.visitMember(typeClass, method);

					} else if (StringUtils.isNotEmpty(typeClass.superName)) {
						return visitMethod(typeClass, new CodeType(typeClass, typeClass.superName), methodName,
								parameterTypes);
					}
				}
			} else {
				return NativeVisiter.visitMethod(clazz, type, methodName, parameterTypes);
			}
		}
		return null;
	}
}
