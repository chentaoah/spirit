package com.gitee.spirit.core.lexer;

import com.gitee.spirit.common.entity.Result;
import com.gitee.spirit.common.enums.StateEnum;
import com.gitee.spirit.core.api.CharAction;
import com.gitee.spirit.core.api.CharsHandler;
import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.CharsContext;

public abstract class AbstractCharsHandler implements CharsHandler, CharAction {

	@Override
	public Result handle(CharsContext context, StringBuilder builder) {
		for (context.index = 0; context.index < builder.length(); context.index++) {
			CharEvent event = new CharEvent(context, builder.charAt(context.index));
			if (this.isTrigger(event)) {
				Result result = this.handle(event);
				if (result != null) {
					if (result.code == StateEnum.RESET.ordinal()) {
						context.index = 0;
					} else if (result.code == StateEnum.BREAK.ordinal()) {
						break;
					}
				}
			}
		}
		return buildResult(context, builder);
	}

	public Result buildResult(CharsContext context, StringBuilder builder) {
		return new Result(builder);
	}

}
