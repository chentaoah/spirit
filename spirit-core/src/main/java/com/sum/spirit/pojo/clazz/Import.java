package com.sum.spirit.pojo.clazz;

import com.sum.spirit.core.lexer.ElementBuilder;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeUtils;

public class Import extends Elemented {

	public Import(Element element) {
		super(element);
	}

	public Import(String className) {
		super(SpringUtils.getBean(ElementBuilder.class).build("import " + className));
	}

	@Override
	public String getName() {
		return getClassName();
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
