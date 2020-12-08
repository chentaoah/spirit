package com.sum.spirit.pojo.common;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;

public class ElementEvent {

	public IClass clazz;
	public MethodContext context;
	public Element element;
	public Statement statement;

	public ElementEvent(IClass clazz, Element element) {
		this.clazz = clazz;
		this.element = element;
	}

	public ElementEvent(IClass clazz, Element element, MethodContext context) {
		this.clazz = clazz;
		this.element = element;
		this.context = context;
	}

	public ElementEvent(IClass clazz, Statement statement) {
		this.clazz = clazz;
		this.statement = statement;
	}

	public ElementEvent(IClass clazz, Statement statement, MethodContext context) {
		this.clazz = clazz;
		this.statement = statement;
		this.context = context;
	}

	public boolean isMethodScope() {
		return context != null;
	}

	public Statement getStatement() {
		return element != null ? element.statement : statement;
	}

}
