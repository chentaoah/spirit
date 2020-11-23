package com.sum.spirit.core.lexerx;

public interface LexerAction {

	boolean isTrigger(char c);

	void pushStack(LexerEvent event);

}
