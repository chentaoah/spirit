package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerResult;

public interface LexerAction {

	boolean isTrigger(CharEvent event);

	LexerResult handle(CharEvent event);

}
