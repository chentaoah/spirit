package com.sum.shy.core.clazz;

import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Line;
import com.sum.shy.core.utils.TypeUtils;

public class Import {

	public Element element;

	public Import(Element element) {
		this.element = element;
	}

	public Import(String className) {
		this.element = new Element(new Line("import " + className));
	}

	public String getClassName() {
		return element.getStr(1);
	}

	public String getTypeName() {
		return TypeUtils.getTypeName(getClassName());
	}

	public boolean hasAlias() {
		return element.contains(2);
	}

	public String getAlias() {
		return hasAlias() ? element.getStr(2) : null;
	}

	public boolean isMatch(String typeName) {
		return hasAlias() ? getAlias().equals(typeName) : getTypeName().equals(typeName);
	}

}
