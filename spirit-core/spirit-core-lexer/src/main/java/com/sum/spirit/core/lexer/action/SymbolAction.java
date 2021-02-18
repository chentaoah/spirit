package com.sum.spirit.core.lexer.action;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.core.lexer.entity.LexerContext;
import com.sum.spirit.core.lexer.entity.LexerEvent;
import com.sum.spirit.core.lexer.entity.Region;

@Component
@Order(-60)
public class SymbolAction extends AbstractLexerAction {

	@Override
	public boolean isTrigger(LexerEvent event) {
		return SymbolEnum.isSymbolChar(event.ch);
	}

	@Override
	public void pushStack(LexerEvent event) {

		LexerContext context = event.context;
		StringBuilder builder = context.builder;
		Map<String, String> replacedStrs = context.replacedStrs;

		// 尝试获取两个字符，判断是否双字符符号
		if (context.index + 1 < builder.length()) {
			String str = builder.substring(context.index, context.index + 2);
			if (SymbolEnum.isDoubleSymbol(str)) {
				Region region = new Region(context.index, context.index + 2);
				replaceStr(builder, region, "@symbol" + context.nameCount++, replacedStrs);
				return;
			}
		}

		// 尝试获取一个字符，判断是否双字符符号
		String str = builder.substring(context.index, context.index + 1);
		if (SymbolEnum.isSingleSymbol(str)) {
			Region region = new Region(context.index, context.index + 1);
			replaceStr(builder, region, "@symbol" + context.nameCount++, replacedStrs);
			return;
		}

		throw new RuntimeException("Symbol that cannot be processed!");
	}

}