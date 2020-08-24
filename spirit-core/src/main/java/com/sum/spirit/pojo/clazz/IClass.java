package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.link.TypeFactory;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.TypeTable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.utils.TypeUtils;

public class IClass extends Importable {

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	public String packageStr;

	public List<IAnnotation> annotations = new ArrayList<>();

	public Element root;

	public List<IField> fields = new ArrayList<>();

	public List<IMethod> methods = new ArrayList<>();

	public boolean isInterface() {
		return root.isInterface();
	}

	public boolean isAbstract() {
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

	@Override
	public String getClassName() {
		return packageStr + "." + getSimpleName();
	}

	public IType toType() {
		return factory.create(this, getTypeToken());

	}

	public IType getSuperType() {// 注意:这里返回的是Super<T,K>
		Token token = root.getKeywordParam(Constants.EXTENDS_KEYWORD);// 这里返回的,可以是泛型格式，而不是className
		if (token != null)
			return factory.create(this, token);
		return TypeTable.OBJECT_TYPE;// 如果不存在继承，则默认是继承Object
	}

	public List<IType> getInterfaceTypes() {
		List<IType> interfaces = new ArrayList<>();
		for (Token token : root.getKeywordParams(Constants.IMPLS_KEYWORD))
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
