package com.sum.shy.clazz.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.type.api.IType;

public abstract class AbsContainer extends AbsAnnotated implements Container {

	// 静态字段
	public List<IField> staticFields = new ArrayList<>();
	// 静态方法
	public List<IMethod> staticMethods = new ArrayList<>();
	// 字段
	public List<IField> fields = new ArrayList<>();
	// 方法
	public List<IMethod> methods = new ArrayList<>();
	// 内部类( typeName --> IClass )
	public Map<String, IClass> coopClasses = new LinkedHashMap<>();

	public void addField(IField field) {
		checkField(field);
		if ("static".equals(field.scope)) {
			staticFields.add(field);
		} else {
			fields.add(field);
		}
	}

	public void addMethod(IMethod method) {
		if ("static".equals(method.scope)) {
			staticMethods.add(method);
		} else {
			methods.add(method);
		}
	}

	public void checkField(IField field) {
		boolean flag = false;
		for (IField f : staticFields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		for (IField f : fields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		if (flag)
			throw new RuntimeException("Cannot have duplicate fields!number:[" + field.stmt.line.number + "], text:[ "
					+ field.stmt.line.text.trim() + " ], var:[" + field.name + "]");
	}

	public boolean existField(String fieldName) {
		for (IField field : staticFields) {
			if (field.name.equals(fieldName)) {
				return true;
			}
		}
		for (IField field : fields) {
			if (field.name.equals(fieldName)) {
				return true;
			}
		}
		return false;
	}

	public IField findField(String fieldName) {
		for (IField field : staticFields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}
		for (IField field : fields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}
		throw new RuntimeException("The field does not exist!class:" + getClassName() + ", field:" + fieldName);

	}

	public boolean existMethod(String methodName, List<IType> paramTypes) {
		for (IMethod method : staticMethods) {
			if (method.isSame(methodName, paramTypes)) {
				return true;
			}
		}
		for (IMethod method : methods) {
			if (method.isSame(methodName, paramTypes)) {
				return true;
			}
		}
		return false;
	}

	public IMethod findMethod(String methodName, List<IType> paramTypes) {
		for (IMethod method : staticMethods) {
			if (method.isSame(methodName, paramTypes)) {
				return method;
			}
		}
		for (IMethod method : methods) {
			if (method.isSame(methodName, paramTypes)) {
				return method;
			}
		}
		throw new RuntimeException("The method does not exist!class:" + getClassName() + ", method:" + methodName);

	}

	public List<Member> getAllMember() {
		List<Member> list = new ArrayList<>();
		list.addAll(staticFields);
		list.addAll(staticMethods);
		list.addAll(fields);
		list.addAll(methods);
		return list;
	}

	protected abstract String getClassName();

}
