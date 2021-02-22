package com.sum.spirit.core.api;

import com.sum.spirit.core.compile.entity.ElementEvent;

public interface ElementAction {

	boolean isTrigger(ElementEvent event);

	void visit(ElementEvent event);

}
