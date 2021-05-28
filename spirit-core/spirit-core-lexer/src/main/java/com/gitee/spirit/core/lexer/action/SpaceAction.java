package com.gitee.spirit.core.lexer.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.LexerAction;
import com.gitee.spirit.core.lexer.entity.CharEvent;
import com.gitee.spirit.core.lexer.entity.CommonResult;
import com.gitee.spirit.core.lexer.entity.CommonState;
import com.gitee.spirit.core.lexer.entity.LexerContext;
import com.gitee.spirit.core.lexer.entity.Region;

@Component
@Order(-40)
public class SpaceAction implements LexerAction {

	@Override
	public boolean isTrigger(CharEvent event) {
		return event.ch == ' ';
	}

	@Override
	public CommonResult handle(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		return new CommonResult(CommonState.SKIP, new Region(context.index, context.index + 1));
	}

}
