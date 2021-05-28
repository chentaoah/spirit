package com.gitee.spirit.core.api;

import com.gitee.spirit.core.compile.entity.ElementEvent;

public interface ElementAction {

	boolean isTrigger(ElementEvent event);

	void handle(ElementEvent event);

}
