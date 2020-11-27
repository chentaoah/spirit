package com.sum.spirit.api;

import com.sum.spirit.pojo.common.LexerEvent;

public interface LexerAction {

	boolean isTrigger(LexerEvent event);

	void pushStack(LexerEvent event);

}
