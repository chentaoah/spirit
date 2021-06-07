package com.gitee.spirit.core.lexer;

import com.gitee.spirit.core.api.CharAction;
import com.gitee.spirit.core.api.CharsHandler;
import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.CharsContext;
import com.gitee.spirit.core.lexer.entity.CommonResult;
import com.gitee.spirit.core.lexer.entity.CommonState;

public abstract class AbstractCharsHandler implements CharsHandler, CharAction {

	@Override
	public CommonResult handle(CharsContext context, StringBuilder builder) {
		for (context.index = 0; context.index < builder.length(); context.index++) {
			CharEvent event = new CharEvent(context, builder.charAt(context.index));
			if (this.isTrigger(event)) {
				CommonResult result = this.handle(event);
				if (result != null) {
					if (result.state == CommonState.RESET) {
						context.index = 0;
					} else if (result.state == CommonState.BREAK) {
						break;
					}
				}
			}
		}
		return buildResult(context, builder);
	}

	public CommonResult buildResult(CharsContext context, StringBuilder builder) {
		return new CommonResult(builder);
	}

}
