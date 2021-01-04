package com.sum.spirit.pojo.clazz.api;

import com.sum.spirit.pojo.element.impl.Element;

public abstract class ElementUnit extends TypeUnit {

	public Element element;

	public ElementUnit(Element element) {
		this.element = element;
	}

}
