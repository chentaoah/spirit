package com.sum.spirit.core.clazz.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.clazz.frame.ImportEntity;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

public class IClass extends ImportEntity {

	public String packageStr;
	public List<IField> fields;
	public List<IMethod> methods;

	public IClass(List<Import> imports, List<IAnnotation> annotations, Element element) {
		super(imports, annotations, element);
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

	public String getSimpleName() {
		return TypeUtils.getTargetName(getTypeToken().toString());
	}

	public String getClassName() {
		return packageStr + "." + getSimpleName();
	}

	public int getTypeVariableIndex(String genericName) {
		String simpleName = getTypeToken().toString();
		// 这样分割，是有风险的，不过一般来说，类型说明里面不会再有嵌套
		List<String> names = TypeUtils.splitName(simpleName);
		names.remove(0);
		int index = 0;
		for (String name : names) {
			if (name.equals(genericName)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public IType getSuperType() {// 注意:这里返回的是Super<T,K>
		Token token = element.getKeywordParam(KeywordEnum.EXTENDS.value);// 这里返回的,可以是泛型格式，而不是className
		if (token != null) {
			return classResolver.getTypeFactory().create(this, token);
		}
		return null;
	}

	public List<IType> getInterfaceTypes() {
		List<IType> interfaces = new ArrayList<>();
		for (Token token : element.getKeywordParams(KeywordEnum.IMPLS.value)) {
			interfaces.add(classResolver.getTypeFactory().create(this, token));
		}
		return interfaces;
	}

	public IField getField(String fieldName) {
		return Lists.findOne(fields, field -> field.getName().equals(fieldName));
	}

	public List<IMethod> getMethods(String methodName) {
		return methods.stream().filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
	}

}
