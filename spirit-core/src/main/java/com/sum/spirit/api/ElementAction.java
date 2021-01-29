package com.sum.spirit.api;

import com.sum.spirit.core.visiter.entity.ElementEvent;

public interface ElementAction {

	boolean isTrigger(ElementEvent event);

	void visit(ElementEvent event);

}
