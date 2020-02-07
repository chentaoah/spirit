package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.doc.Document;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.type.api.IType;

public class IClass {

	public Document document;

	public String packageStr;

	public List<Element> imports = new ArrayList<>();

	public List<Element> annotations = new ArrayList<>();

	public Element root;

	public List<IField> fields = new ArrayList<>();

	public List<IMethod> methods = new ArrayList<>();

	public List<IClass> coopClasses = new ArrayList<>();

	public String findImport(String simpleName) {
		return null;
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

	public IField getField(String name) {

		return null;
	}

	public IMethod getMethod(String name, List<IType> types) {

		return null;
	}

	public List<AbsMember> getAllMembers() {
		List<AbsMember> members = new ArrayList<>();
		members.addAll(getFields());
		members.addAll(getMethods());
		return members;
	}

}
