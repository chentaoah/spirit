package com.sum.spirit.core.visiter.pojo;

import com.sum.spirit.core.clazz.pojo.IClass;
import com.sum.spirit.core.element.pojo.Element;

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
