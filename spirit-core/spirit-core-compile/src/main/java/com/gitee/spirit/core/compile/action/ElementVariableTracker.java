package com.gitee.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.annotation.App;
import com.gitee.spirit.core.compile.deduce.VariableTracker;
import com.gitee.spirit.core.compile.entity.ElementEvent;

@App
@Component
@Order(-60)
public class ElementVariableTracker extends AbstractElementAction {

	@Autowired
	public VariableTracker tracker;

	@Override
	public void handle(ElementEvent event) {
		tracker.visit(event.clazz, event.context, event.element);
	}

}
