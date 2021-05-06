package com.sum.spirit.core.clazz.frame;

import com.sum.spirit.core.element.entity.Element;

public abstract class ElementEntity extends TypeEntity {

	public Element element;

	public ElementEntity(Element element) {
		this.element = element;
	}

}
