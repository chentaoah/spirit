package com.gitee.spirit.core.compile.action;

import com.gitee.spirit.core.api.ElementAction;
import com.gitee.spirit.core.compile.entity.ElementEvent;

public abstract class AbstractElementAction implements ElementAction {

	@Override
	public boolean isTrigger(ElementEvent event) {
		return event.element != null;
	}

}
