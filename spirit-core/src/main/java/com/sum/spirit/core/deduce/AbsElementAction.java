package com.sum.spirit.core.deduce;

import com.sum.spirit.api.ElementAction;
import com.sum.spirit.pojo.common.ElementEvent;

public abstract class AbsElementAction implements ElementAction {

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
