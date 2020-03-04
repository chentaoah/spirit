package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.doc.Document;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.core.utils.ReflectUtils;
import com.sum.shy.core.utils.TypeUtils;

public class IClass {

	public Document document;

	public String packageStr;

	public List<Import> imports = new ArrayList<>();

	public List<Element> annotations = new ArrayList<>();

	public Element root;

	public List<IField> fields = new ArrayList<>();

	public List<IMethod> methods = new ArrayList<>();

	public List<IClass> coopClasses = new ArrayList<>();

	public String findImport(String simpleName) {
		// 如果本身传入的就是一个全名的话，直接返回
		if (simpleName.contains("."))
			return simpleName;

		// 如果传进来是个数组，那么处理一下
		boolean isArray = TypeUtils.isArray(simpleName);
		String typeName = TypeUtils.getTypeName(simpleName);

		// 1.首先先去引入里面找
		for (Import iImport : imports) {
			if (iImport.name.equals(typeName))
				return iImport.className;
		}

		String className = null;
		// 2.友元,注意这个类本身也在所有类之中
		if (className == null)
			className = Context.get().findFriend(typeName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

		// 3.内部类
		for (IClass clazz : coopClasses) {
			if (clazz.getTypeName().equals(typeName))
				return clazz.getClassName();
		}

		// 4.如果没有引入的话，可能是一些基本类型java.lang包下的
		if (className == null)
			className = ReflectUtils.getClassName(simpleName);
		if (className == null)
			className = ReflectUtils.getCollectionType(typeName);
		if (className != null)
			return className;

		// 如果一直没有找到就抛出异常
		throw new RuntimeException("No import info found!simpleName:[" + simpleName + "]");
	}

	public boolean addImport(String className) {
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

	public String getTypeName() {
		if (root == null)
			return document.name;
		return root.getKeywordParam(Constants.CLASS_KEYWORD);
	}

	public String getSuperName() {
		return root.getKeywordParam(Constants.EXTENDS_KEYWORD);
	}

	public List<String> getInterfaces() {
		return root.getKeywordParams(Constants.IMPL_KEYWORD);
	}

	public String getClassName() {
		return packageStr + "." + getTypeName();
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

	public List<AbsMember> getAllMembers() {
		List<AbsMember> members = new ArrayList<>();
		members.addAll(getFields());
		members.addAll(getMethods());
		return members;
	}

	public void debug() {
		// TODO Auto-generated method stub

	}

}
