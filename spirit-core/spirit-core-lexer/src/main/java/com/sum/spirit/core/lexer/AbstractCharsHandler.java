package com.sum.spirit.core.lexer;

import com.sum.spirit.core.api.CharAction;
import com.sum.spirit.core.api.CharsHandler;
import com.sum.spirit.core.lexer.action.AbstractLexerAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;

public abstract class AbstractCharsHandler extends AbstractLexerAction implements CharsHandler, CharAction {

	@Override
	public void handle(CharsContext context, StringBuilder builder) {
		for (; context.index < builder.length(); context.index++) {
			char ch = builder.charAt(context.index);
			CharEvent event = new CharEvent(context, ch);
			if (isTrigger(event)) {
				handle(event);
			}
		}
	}

}
