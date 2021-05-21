package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.CharEvent;

public interface CharAction<T> {

	boolean isTrigger(CharEvent event);

	T handle(CharEvent event);

}
