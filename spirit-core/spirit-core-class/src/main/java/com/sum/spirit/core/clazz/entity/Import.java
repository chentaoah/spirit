package com.sum.spirit.core.clazz.entity;

import com.sum.spirit.core.clazz.frame.ElementEntity;
import com.sum.spirit.core.clazz.utils.TypeUtils;
import com.sum.spirit.core.element.entity.Element;

public class Import extends ElementEntity {

	public Import(Element element) {
		super(element);
	}

	public String getClassName() {
		return element.getStr(1);
	}

	public String getLastName() {
		return TypeUtils.getLastName(getClassName());
	}

	public boolean hasAlias() {
		return element.contains(2);
	}

	public String getAlias() {
		return hasAlias() ? element.getStr(2) : null;
	}

	public boolean matchSimpleName(String simpleName) {
		return !hasAlias() ? getLastName().equals(simpleName) : getAlias().equals(simpleName);
	}

	public boolean matchClassName(String className) {
		return getClassName().equals(className);
	}

	public boolean matchStaticSourceName(String staticSourceName) {
		return element.getStr(2).equals(staticSourceName);
	}

}
