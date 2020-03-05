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

	public List<IAnnotation> annotations = new ArrayList<>();

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
			if (iImport.isMatch(typeName))
				return iImport.className;
		}

		// 2.如果是本身，则直接返回本身
		if (getTypeName().equals(typeName)) {
			return getClassName();
		}

		// 3.内部类
		for (IClass clazz : coopClasses) {
			if (clazz.getTypeName().equals(typeName))
				return clazz.getClassName();
		}

		String className = null;
		// 4.友元,注意这个类本身也在所有类之中
		if (className == null)
			className = Context.get().findFriend(typeName);
		if (className != null)
			return !isArray ? className : "[L" + className + ";";

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

		// 5.内部类不添加
		for (IClass clazz : coopClasses) {
			if (clazz.getClassName().equals(className))
				return true;
		}

		// 6.如果引入了，则不必再添加了
		// 7.如果没有引入，但是typeName相同，则无法引入
		for (Import iImport : imports) {
			if (!iImport.hasAlias() && iImport.className.equals(className)) {// 重复添加，也是成功
				return true;
			} else if (!iImport.hasAlias() && iImport.name.equals(typeName)) {
				return false;
			}
		}

		imports.add(new Import(className, typeName));
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
