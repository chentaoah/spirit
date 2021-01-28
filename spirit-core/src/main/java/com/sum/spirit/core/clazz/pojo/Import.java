package com.sum.spirit.core.clazz.pojo;

import com.sum.spirit.core.clazz.pojo.api.ElementUnit;
import com.sum.spirit.core.element.ElementBuilder;
import com.sum.spirit.core.element.pojo.Element;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeUtils;

public class Import extends ElementUnit {

	public Import(Element element) {
		super(element);
	}

	public Import(String className) {
		super(SpringUtils.getBean(ElementBuilder.class).build("import " + className));
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

}
