package com.sum.spirit.lexer.api;

import com.sum.spirit.lexer.entity.LexerEvent;

public interface LexerAction {

	boolean isTrigger(LexerEvent event);

	void pushStack(LexerEvent event);

}
