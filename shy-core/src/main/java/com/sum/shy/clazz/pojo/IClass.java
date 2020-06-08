package com.sum.shy.clazz.pojo;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.pojo.Constants;
import com.sum.shy.core.pojo.Context;
import com.sum.shy.core.pojo.StaticType;
import com.sum.shy.document.pojo.Element;
import com.sum.shy.document.pojo.Token;
import com.sum.shy.lib.Assert;
import com.sum.shy.member.deducer.TypeFactory;
import com.sum.shy.utils.TypeUtils;

public class IClass {

	public String packageStr;

	public List<Import> imports = new ArrayList<>();

	public List<IAnnotation> annotations = new ArrayList<>();

	public Element root;

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
		className = TypeUtils.getClassName(simpleName);
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
		if (TypeUtils.isPrimitive(targetName) || targetName.equals("java.lang." + lastName))
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

	public boolean isInterface() {// 接口里不允许嵌套别的东西
		return root.isInterface();
	}

	public boolean isAbstract() {// 抽象类里也不允许嵌套
		return root.isAbstract();
	}

	public boolean isClass() {
		return root.isClass();
	}

	public Token getTypeToken() {
		Token token = null;
		if (isInterface()) {
			token = root.getKeywordParam(Constants.INTERFACE_KEYWORD);
		} else if (isAbstract() || isClass()) {
			token = root.getKeywordParam(Constants.CLASS_KEYWORD, Constants.ABSTRACT_KEYWORD);
		}
		Assert.isTrue(token != null && token.isType(), "Cannot get type token of class!");
		return token;
	}

	public String getSimpleName() {
		return TypeUtils.getTargetName(getTypeToken().toString());
	}

	public IType getTypeVariable(String genericName) {// 这里要小心，泛型参数多层嵌套的情况
		String simpleName = getTypeToken().toString();
		List<String> names = TypeUtils.splitName(simpleName);
		names.remove(0);
		for (String name : names) {
			if (name.equals(genericName)) {
				IType type = TypeFactory.create(Object.class);
				type.setGenericName(genericName);
				return type;
			}
		}
		return null;
	}

	public String getClassName() {
		return packageStr + "." + getSimpleName();
	}

	public IType toType() {
		return TypeFactory.create(getClassName());
	}

	public IType getSuperType() {
		Token token = root.getKeywordParam(Constants.EXTENDS_KEYWORD);// 这里返回的,可以是泛型格式，而不是className
		if (token != null)
			return TypeFactory.create(this, token);
		return StaticType.OBJECT_TYPE;// 如果不存在继承，则默认是继承Object
	}

	public List<IType> getInterfaces() {
		List<IType> interfaces = new ArrayList<>();
		for (Token token : root.getKeywordParams(Constants.IMPL_KEYWORD))
			interfaces.add(TypeFactory.create(this, token));
		return interfaces;
	}

	public List<IField> getFields() {
		return fields;
	}

	public List<IMethod> getMethods() {
		return methods;
	}

	public boolean existField(String fieldName) {
		return getField(fieldName) != null;
	}

	public IField getField(String fieldName) {
		for (IField field : fields) {
			if (field.name.equals(fieldName))
				return field;
		}
		return null;
	}

	public boolean existMethod(String methodName, List<IType> parameterTypes) {
		return getMethod(methodName, parameterTypes) != null;
	}

	public IMethod getMethod(String methodName, List<IType> parameterTypes) {
		for (IMethod method : methods) {
			if (method.isMatch(methodName, parameterTypes))
				return method;
		}
		return null;
	}

	public void debug() {
		// TODO Auto-generated method stub
	}

}
