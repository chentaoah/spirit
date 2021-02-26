package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.CharEvent;

public interface CharAction {

	boolean isTrigger(CharEvent event);

	void handle(CharEvent event);

}
