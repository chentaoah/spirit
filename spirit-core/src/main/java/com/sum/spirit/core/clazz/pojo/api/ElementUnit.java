package com.sum.spirit.core.clazz.pojo.api;

import com.sum.spirit.core.element.pojo.Element;

public abstract class ElementUnit extends TypeUnit {

	public Element element;

	public ElementUnit(Element element) {
		this.element = element;
	}

}
