package com.sum.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.compile.deduce.VariableTracker;
import com.sum.spirit.core.compile.entity.ElementEvent;

@Component
@Order(-60)
public class ElementVariableTracker extends AbstractElementAction {

	@Autowired
	public VariableTracker tracker;

	@Override
	public void visit(ElementEvent event) {
		tracker.visit(event.clazz, event.context, event.element);
	}

}
