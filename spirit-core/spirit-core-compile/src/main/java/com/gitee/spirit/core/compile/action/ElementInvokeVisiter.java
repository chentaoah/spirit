package com.gitee.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.annotation.App;
import com.gitee.spirit.core.compile.deduce.InvocationVisiter;
import com.gitee.spirit.core.compile.entity.ElementEvent;

@App
@Component
@Order(-40)
public class ElementInvokeVisiter extends AbstractElementAction {

	@Autowired
	public InvocationVisiter visiter;

	@Override
	public void handle(ElementEvent event) {
		visiter.visit(event.clazz, event.element);
	}

}
