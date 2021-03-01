package com.sum.spirit.core.lexer;

import com.sum.spirit.core.api.CharAction;
import com.sum.spirit.core.api.CharsHandler;
import com.sum.spirit.core.lexer.action.AbstractCharAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;

public abstract class AbstractCharsHandler extends AbstractCharAction implements CharsHandler, CharAction {

	@Override
	public void handle(CharsContext context, StringBuilder builder, CharAction action) {
		for (; context.index < builder.length(); context.index++) {
			char ch = builder.charAt(context.index);
			CharEvent event = new CharEvent(context, ch);
			if (action.isTrigger(event)) {
				action.handle(event);
			}
		}
	}

	@Override
	public void handle(CharsContext context, StringBuilder builder) {
		handle(context, builder, this);
	}

}
