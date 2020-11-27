package com.sum.spirit.pojo.clazz.frame;

import com.sum.spirit.pojo.element.Element;

public abstract class Elemented extends Typed {

	public Element element;

	public Elemented(Element element) {
		this.element = element;
	}

	public abstract String getName();

}
