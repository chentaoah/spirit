package com.sum.spirit.core.lexer;

import com.sum.spirit.core.api.CharAction;
import com.sum.spirit.core.api.CharsHandler;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CharsContext;
import com.sum.spirit.core.lexer.entity.CommonResult;

public abstract class AbstractCharsHandler implements CharsHandler, CharAction {

	@Override
	public CommonResult handle(CharsContext context, StringBuilder builder) {
		for (context.index = 0; context.index < builder.length(); context.index++) {
			CharEvent event = new CharEvent(context, builder.charAt(context.index));
			if (this.isTrigger(event)) {
				this.handle(event);
			}
		}
		return buildResult(context, builder);
	}

	public CommonResult buildResult(CharsContext context, StringBuilder builder) {
		return new CommonResult(builder);
	}

}
