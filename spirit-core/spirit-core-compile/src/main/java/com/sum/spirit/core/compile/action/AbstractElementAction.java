package com.sum.spirit.core.compile.action;

import com.sum.spirit.core.api.ElementAction;
import com.sum.spirit.core.compile.entity.ElementEvent;

public abstract class AbstractElementAction implements ElementAction {

	@Override
	public boolean isTrigger(ElementEvent event) {
		return event.element != null;
	}

}
