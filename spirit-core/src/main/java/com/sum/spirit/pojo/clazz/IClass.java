package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.core.type.TypeFactory;
import com.sum.spirit.pojo.clazz.frame.Imported;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.pojo.type.IType;
import com.sum.spirit.utils.Assert;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeUtils;

public class IClass extends Imported {

	public String packageStr;

	public List<IField> fields;

	public List<IMethod> methods;

	public IClass(List<Import> imports, List<IAnnotation> annotations, Element element) {
		super(imports, annotations, element);
	}

	@Override
	public String getName() {
		return getSimpleName();
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

	@Override
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
		return TypeEnum.Object.value;// 如果不存在继承，则默认是继承Object
	}

	public List<IType> getInterfaceTypes() {
		TypeFactory factory = SpringUtils.getBean(TypeFactory.class);
		List<IType> interfaces = new ArrayList<>();
		for (Token token : element.getKeywordParams(KeywordEnum.IMPLS.value))
			interfaces.add(factory.create(this, token));
		return interfaces;
	}

	public IField getField(String fieldName) {
		for (IField field : fields) {
			if (field.getName().equals(fieldName))
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
