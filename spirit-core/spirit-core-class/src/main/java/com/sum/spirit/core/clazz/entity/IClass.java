package com.sum.spirit.core.clazz.entity;

import java.util.List;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.clazz.frame.ImportUnit;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

public class IClass extends ImportUnit {

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

	public String getSimpleName() {
		return TypeUtils.getTargetName(getTypeToken().toString());
	}

	public String getClassName() {
		return packageStr + "." + getSimpleName();
	}

	public IField getField(String fieldName) {
		return Lists.findOne(fields, field -> field.getName().equals(fieldName));
	}

}
