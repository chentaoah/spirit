package com.sum.spirit.pojo.clazz.api;

import com.sum.spirit.pojo.element.impl.Element;

public abstract class Elemented extends Typed {

	public Element element;

	public Elemented(Element element) {
		this.element = element;
	}

}
