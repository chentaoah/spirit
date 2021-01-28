package com.sum.spirit.api;

import com.sum.spirit.core.element.action.lexer.LexerEvent;

public interface LexerAction {

	boolean isTrigger(LexerEvent event);

	void pushStack(LexerEvent event);

}
