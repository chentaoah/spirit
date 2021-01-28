package com.sum.spirit.api;

import com.sum.spirit.core.lexer.pojo.LexerEvent;

public interface LexerAction {

	boolean isTrigger(LexerEvent event);

	void pushStack(LexerEvent event);

}
