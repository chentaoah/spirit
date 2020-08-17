package com.sum.shy.pojo.clazz;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.lexer.ElementBuilder;
import com.sum.shy.pojo.element.Element;
import com.sum.shy.utils.TypeUtils;

public class Import {

	public static ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	public Element element;

	public Import(Element element) {
		this.element = element;
	}

	public Import(String className) {
		this.element = builder.build("import " + className);
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
