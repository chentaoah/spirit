package com.sum.spirit.core.visiter.handler;

import com.sum.spirit.api.ElementAction;
import com.sum.spirit.core.visiter.pojo.ElementEvent;

public abstract class AbstractElementAction implements ElementAction {

	@Override
	public boolean isTrigger(ElementEvent event) {
		return event.element != null;
	}

	@Override
	public void visit(ElementEvent event) {
		if (!event.isMethodScope()) {
			visitFieldScope(event);
		} else {
			visitMethodScope(event);
		}
	}

	public void visitFieldScope(ElementEvent event) {
		// ignore
	}

	public void visitMethodScope(ElementEvent event) {
		// ignore
	}

}
