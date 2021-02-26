package com.sum.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.compile.deduce.InvocationVisiter;
import com.sum.spirit.core.compile.entity.ElementEvent;

@Component
@Order(-40)
public class ElementInvokeVisiter extends AbstractElementAction {

	@Autowired
	public InvocationVisiter visiter;

	@Override
	public void visit(ElementEvent event) {
		visiter.visit(event.clazz, event.element);
	}

}
