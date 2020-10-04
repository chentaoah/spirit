package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.api.link.TypeFactory;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.common.Context;
import com.sum.spirit.pojo.common.KeywordEnum;
import com.sum.spirit.pojo.common.TypeTable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeUtils;

public class IClass {

	public String packageStr;

	public List<Import> imports = new ArrayList<>();

	public List<IAnnotation> annotations = new ArrayList<>();

	public Element element;

	public List<IField> fields = new ArrayList<>();

	public List<IMethod> methods = new ArrayList<>();

	public String findImport(String simpleName) {
		// 如果是className，则直接返回
		if (simpleName.contains("."))
			return simpleName;

		// 如果传进来是个数组，那么处理一下
		String targetName = TypeUtils.getTargetName(simpleName);
		boolean isArray = TypeUtils.isArray(simpleName);

		// 1.首先先去引入里面找
		for (Import imp : imports) {
			if (imp.isMatch(targetName))
				return !isArray ? imp.getClassName() : "[L" + imp.getClassName() + ";";
		}

		// 2.在所有类里面找，包括这个类本身也在其中
		String className = Context.get().getClassName(targetName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

		// 3.如果是基本类型，基本类型数组，或者java.lang.下的类，则直接返回
		className = TypeTable.getClassName(simpleName);
		if (className != null)
			return className;

		// 如果一直没有找到就抛出异常
		throw new RuntimeException("No import info found!simpleName:[" + simpleName + "]");
	}

	public boolean addImport(String className) {

		// 如果是数组，则把修饰符号去掉
		String targetName = TypeUtils.getTargetName(className);
		String lastName = TypeUtils.getLastName(className);

		// 1. 基本类型不添加和java.lang.包下不添加
		if (TypeTable.isPrimitive(targetName) || targetName.equals("java.lang." + lastName))
			return true;

		// 2.如果是本身,不添加
		if (getClassName().equals(targetName))
			return true;

		// 3.如果引入了，则不必再添加了
		// 4.如果没有引入，但是typeName相同，则无法引入
		for (Import imp : imports) {
			if (!imp.hasAlias()) {
				if (imp.getClassName().equals(targetName)) {
					return true;

				} else if (imp.getLastName().equals(lastName)) {
					return false;
				}
			} else {// 如果是别名，则不用添加了
				if (imp.getClassName().equals(className))
					return false;
			}
		}
		imports.add(new Import(targetName));
		return true;
	}

	public IAnnotation getAnnotation(String className) {
		for (IAnnotation annotation : annotations) {
			if (annotation.type.getClassName().equals(className))
				return annotation;
		}
		return null;
	}

	public boolean isInterface() {
		return element.isInterface();
	}

	public boolean isAbstract() {
		return element.isAbstract();
	}

	public boolean isClass() {
		return element.isClass();
	}

	public Token getTypeToken() {
		Token token = null;
		if (isInterface()) {
			token = element.getKeywordParam(KeywordEnum.INTERFACE.value);
		} else if (isAbstract() || isClass()) {
			token = element.getKeywordParam(KeywordEnum.CLASS.value, KeywordEnum.ABSTRACT.value);
		}
		Assert.isTrue(token != null && token.isType(), "Cannot get type token of class!");
		return token;
	}

	public int getTypeVariableIndex(String genericName) {
		String simpleName = getTypeToken().toString();
		List<String> names = TypeUtils.splitName(simpleName);
		names.remove(0);
		int index = 0;
		for (String name : names) {
			if (name.equals(genericName))
				return index;
			index++;
		}
		return -1;
	}

	public String getSimpleName() {
		return TypeUtils.getTargetName(getTypeToken().toString());
	}

	public String getClassName() {
		return packageStr + "." + getSimpleName();
	}

	public IType toType() {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		return factory.create(this, getTypeToken());

	}

	public IType getSuperType() {// 注意:这里返回的是Super<T,K>
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		Token token = element.getKeywordParam(KeywordEnum.EXTENDS.value);// 这里返回的,可以是泛型格式，而不是className
		if (token != null)
			return factory.create(this, token);
		return TypeTable.OBJECT_TYPE;// 如果不存在继承，则默认是继承Object
	}

	public List<IType> getInterfaceTypes() {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		List<IType> interfaces = new ArrayList<>();
		for (Token token : element.getKeywordParams(KeywordEnum.IMPLS.value))
			interfaces.add(factory.create(this, token));
		return interfaces;
	}

	public List<IField> getFields() {
		return fields;
	}

	public List<IMethod> getMethods() {
		return methods;
	}

	public IField getField(String fieldName) {
		for (IField field : fields) {
			if (field.name.equals(fieldName))
				return field;
		}
		return null;
	}

	public IMethod getMethod(IType type, String methodName, List<IType> parameterTypes) {
		for (IMethod method : methods) {
			if (method.isMatch(type, methodName, parameterTypes))
				return method;
		}
		return null;
	}

}
