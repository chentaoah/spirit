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

	@Override
	public void handle(CharEvent event) {

		LexerContext context = (LexerContext) event.context;
		StringBuilder builder = context.builder;

		// 尝试获取两个字符，判断是否双字符符号
		if (context.index + 1 < builder.length()) {
			String str = builder.substring(context.index, context.index + 2);
			if (SymbolEnum.isDoubleSymbol(str)) {
				Region region = new Region(context.index, context.index + 2);
				replaceRegion(builder, region, "@symbol" + context.nameCount++, context.replacedStrs);
				return;
			}
		}

		// 尝试获取一个字符，判断是否单字符符号
		String str = builder.substring(context.index, context.index + 1);
		if (SymbolEnum.isSingleSymbol(str)) {
			Region region = new Region(context.index, context.index + 1);
			replaceRegion(builder, region, "@symbol" + context.nameCount++, context.replacedStrs);
			return;
		}

		throw new RuntimeException("Unable to process symbol!");
	}

}