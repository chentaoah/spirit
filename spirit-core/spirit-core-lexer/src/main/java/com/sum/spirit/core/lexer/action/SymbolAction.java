package com.sum.spirit.core.lexer.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.core.api.LexerAction;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.CommonState;
import com.sum.spirit.core.lexer.entity.CommonResult;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.Region;

@Component
@Order(-60)
public class SymbolAction implements LexerAction {

	@Override
	public boolean isTrigger(CharEvent event) {
		return SymbolEnum.isSymbolChar(event.ch);
	}

	@Override
	public CommonResult handle(CharEvent event) {
		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;

		if (context.index + 1 < builder.length()) {
			String str = builder.substring(context.index, context.index + 2);
			if (SymbolEnum.isDoubleSymbol(str)) {
				Region region = new Region(context.index, context.index + 2);
				return new CommonResult(CommonState.SKIP, region);
			}
		}

		String str = builder.substring(context.index, context.index + 1);
		if (SymbolEnum.isSingleSymbol(str)) {
			Region region = new Region(context.index, context.index + 1);
			return new CommonResult(CommonState.SKIP, region);
		}

		throw new RuntimeException("Unhandled branch!");
	}

}