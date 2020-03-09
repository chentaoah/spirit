package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.document.Document;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.core.utils.ReflectUtils;
import com.sum.shy.core.utils.TypeUtils;

public class IClass {

	public Document document;

	public String packageStr;

	public List<Import> imports = new ArrayList<>();

	public List<IAnnotation> annotations = new ArrayList<>();

	public Element root;// 这个可能为null

	public List<IField> fields = new ArrayList<>();

	public List<IMethod> methods = new ArrayList<>();

	public String findImport(String simpleName) {
		// 如果本身传入的就是一个全名的话，直接返回
		if (simpleName.contains("."))
			return simpleName;

		// 如果传进来是个数组，那么处理一下
		boolean isArray = TypeUtils.isArray(simpleName);
		String typeName = TypeUtils.getTypeName(simpleName);

		// 1.首先先去引入里面找
		for (Import iImport : imports) {
			if (iImport.isMatch(typeName))
				return iImport.className;
		}

		String className = null;
		// 2.在所有类里面找，包括这个类本身也在其中
		if (className == null)
			className = Context.get().findFriend(typeName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

		// 3.如果没有引入的话，可能是一些基本类型java.lang包下的
		if (className == null)
			className = ReflectUtils.getClassName(simpleName);
		if (className == null)
			className = ReflectUtils.getCollectionType(typeName);
		if (className == null) {
			try {
				className = ReflectUtils.getClass("java.lang." + typeName).getName();
			} catch (Exception e) {
				// ignore
			}
		}
		if (className != null)
			return className;

		// 如果一直没有找到就抛出异常
		throw new RuntimeException("No import info found!simpleName:[" + simpleName + "]");
	}

	public boolean addImport(String className) {
		// 1.基本类型数组,不添加
		if (className.startsWith("[") && !className.startsWith("[L"))
			return true;

		// 如果是内部类 xxx.xxx.xxx$xxx
		className = className.replaceAll("\\$", ".");
		// 如果是数组，则把修饰符号去掉
		className = TypeUtils.removeDecoration(className);
		// 获取类名
		String typeName = TypeUtils.getTypeNameByClassName(className);

		// 2.基本类className和simpleName相同
		// 3.一般java.lang.包下的类不用引入
		if (ReflectUtils.getClassName(className) != null || className.startsWith("java.lang."))
			return true;

		// 4.如果是本身,不添加
		if (getClassName().equals(className))
			return true;

		// 5.如果引入了，则不必再添加了
		// 6.如果没有引入，但是typeName相同，则无法引入
		for (Import iImport : imports) {
			if (!iImport.hasAlias()) {
				if (iImport.className.equals(className)) {// 重复添加，也是成功
					return true;
				} else if (iImport.name.equals(typeName)) {
					return false;
				}
			}
		}

		imports.add(new Import(className, typeName));
		return true;
	}

	public boolean isInterface() {// 接口里不允许嵌套别的东西
		return root != null && root.isInterface();
	}

	public boolean isAbstract() {// 抽象类里也不允许嵌套
		return root != null && root.isAbstract();
	}

	public boolean isClass() {
		return root == null || root.isClass();
	}

	public String getTypeName() {
		if (root == null)
			return document.name;
		return root.getKeywordParam(Constants.CLASS_KEYWORD);
	}

	public String getSuperName() {
		return root != null ? root.getKeywordParam(Constants.EXTENDS_KEYWORD) : null;
	}

	public List<String> getInterfaces() {
		return root != null ? root.getKeywordParams(Constants.IMPL_KEYWORD) : null;
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
