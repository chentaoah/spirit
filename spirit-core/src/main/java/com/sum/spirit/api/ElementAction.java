package com.sum.spirit.api;

import com.sum.spirit.pojo.common.ElementEvent;

public interface ElementAction {

	boolean isTrigger(ElementEvent event);

	void visit(ElementEvent event);

}
