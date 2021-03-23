package com.sum.spirit.core.lexer.action;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.core.lexer.entity.CharEvent;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.Region;

@Component
@Order(-80)
public class SymbolAction extends AbstractLexerAction {

	@Override
	public boolean isTrigger(CharEvent event) {
		return SymbolEnum.isSymbolChar(event.ch);
	}

	/**
	 * -尝试拆分双字符和单字符符号
	 */
	@Override
	public void handle(CharEvent event) {

		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;

		if (context.index + 1 < builder.length()) {
			String str = builder.substring(context.index, context.index + 2);
			if (SymbolEnum.isDoubleSymbol(str)) {
				Region region = new Region(context.index, context.index + 2);
				replaceRegion(builder, region, "@symbol" + context.nameCount++, context.replacedStrs);
				return;
			}
		}

		String str = builder.substring(context.index, context.index + 1);
		if (SymbolEnum.isSingleSymbol(str)) {
			Region region = new Region(context.index, context.index + 1);
			replaceRegion(builder, region, "@symbol" + context.nameCount++, context.replacedStrs);
			return;
		}

		throw new RuntimeException("Unable to process symbol!");
	}

}