package com.sum.spirit.core.api;

import com.sum.spirit.core.lexer.entity.LexerEvent;

public interface LexerAction {

	boolean isTrigger(LexerEvent event);

	void pushStack(LexerEvent event);

}
