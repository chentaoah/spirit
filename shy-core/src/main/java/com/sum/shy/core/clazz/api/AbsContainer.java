package com.sum.shy.core.clazz.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtField;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.type.api.Type;

public abstract class AbsContainer extends AbsAnnotated implements Container {

	// 静态字段
	public List<CtField> staticFields = new ArrayList<>();
	// 静态方法
	public List<CtMethod> staticMethods = new ArrayList<>();
	// 字段
	public List<CtField> fields = new ArrayList<>();
	// 方法
	public List<CtMethod> methods = new ArrayList<>();
	// 内部类( typeName --> CtClass )
	public Map<String, CtClass> innerClasses = new LinkedHashMap<>();

	public void addField(CtField field) {
		checkField(field);
		if ("static".equals(field.scope)) {
			staticFields.add(field);
		} else {
			fields.add(field);
		}
	}

	public void addMethod(CtMethod method) {
		if ("static".equals(method.scope)) {
			staticMethods.add(method);
		} else {
			methods.add(method);
		}
	}

	public void checkField(CtField field) {
		boolean flag = false;
		for (CtField f : staticFields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		for (CtField f : fields) {
			if (f.name.equals(field.name)) {
				flag = true;
			}
		}
		if (flag)
			throw new RuntimeException("Cannot have duplicate fields!number:[" + field.stmt.line.number + "], text:[ "
					+ field.stmt.line.text.trim() + " ], var:[" + field.name + "]");
	}

	public boolean existField(String fieldName) {
		for (CtField field : staticFields) {
			if (field.name.equals(fieldName)) {
				return true;
			}
		}
		for (CtField field : fields) {
			if (field.name.equals(fieldName)) {
				return true;
			}
		}
		return false;
	}

	public CtField findField(String fieldName) {
		for (CtField field : staticFields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}
		for (CtField field : fields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}
		throw new RuntimeException("The field does not exist!class:" + getClassName() + ", field:" + fieldName);

	}

	public boolean existMethod(String methodName, List<Type> parameterTypes) {
		for (CtMethod method : staticMethods) {
			if (method.isSame(methodName, parameterTypes)) {
				return true;
			}
		}
		for (CtMethod method : methods) {
			if (method.isSame(methodName, parameterTypes)) {
				return true;
			}
		}
		return false;
	}

	public CtMethod findMethod(String methodName, List<Type> parameterTypes) {
		for (CtMethod method : staticMethods) {
			if (method.isSame(methodName, parameterTypes)) {
				return method;
			}
		}
		for (CtMethod method : methods) {
			if (method.isSame(methodName, parameterTypes)) {
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
