package com.sum.spirit.pojo.common;

import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.element.impl.Element;

public class ElementEvent {

	public IClass clazz;
	public MethodContext context;
	public Element element;

	public ElementEvent(IClass clazz, Element element) {
		this.clazz = clazz;
		this.element = element;
	}

	public ElementEvent(IClass clazz, Element element, MethodContext context) {
		this.clazz = clazz;
		this.element = element;
		this.context = context;
	}

	public boolean isMethodScope() {
		return context != null;
	}

}
