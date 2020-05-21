package com.sum.shy.core.clazz;

import com.sum.shy.core.stmt.Element;
import com.sum.shy.core.utils.TypeUtils;

public class Import {

	public Element element;

	public Import(Element element) {
		this.element = element;
	}

	public Import(String className) {
		this.element = new Element("import " + className);
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

	public boolean isMatch(String lastName) {
		return hasAlias() ? getAlias().equals(lastName) : getLastName().equals(lastName);
	}

}
