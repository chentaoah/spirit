package com.sum.spirit.core.compile.action;

import com.sum.spirit.core.compile.entity.ElementEvent;

public abstract class AbstractScopeElementAction extends AbstractElementAction {

	@Override
	public void handle(ElementEvent event) {
		if (event.isFieldScope()) {
			visitFieldScope(event);

		} else if (event.isMethodScope()) {
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
