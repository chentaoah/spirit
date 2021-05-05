package com.sum.spirit.core.lexer;

import com.sum.spirit.core.api.CharAction;
import com.sum.spirit.core.api.CharsHandler;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CharsResult;

public abstract class AbstractCharsHandler implements CharsHandler, CharAction {

	@Override
	public CharsResult handle(CharsContext context, StringBuilder builder) {
		for (; context.index < builder.length(); context.index++) {
			char ch = builder.charAt(context.index);
			CharEvent event = new CharEvent(context, ch);
			if (this.isTrigger(event)) {
				this.handle(event);
			}
		}
		return buildResult(context, builder);

	}

	public CharsResult buildResult(CharsContext context, StringBuilder builder) {
		return new CharsResult(builder);
	}

}
