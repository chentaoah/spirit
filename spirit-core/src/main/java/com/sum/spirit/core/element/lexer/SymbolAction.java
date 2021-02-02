package com.sum.spirit.core.element.lexer;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.common.enums.SymbolEnum;

@Component
@Order(-80)
public class SymbolAction extends AbstractLexerAction {

	@Override
	public boolean isTrigger(LexerEvent event) {
		return SymbolEnum.isSymbolChar(event.char0);
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
				replaceStr(builder, context.index, context.index + 2, "@symbol" + context.nameCount++, replacedStrs);
				return;
			}
		}

		// 尝试获取一个字符，判断是否双字符符号
		String str = builder.substring(context.index, context.index + 1);
		if (SymbolEnum.isSingleSymbol(str)) {
			replaceStr(builder, context.index, context.index + 1, "@symbol" + context.nameCount++, replacedStrs);
			return;
		}

		throw new RuntimeException("Symbol that cannot be processed!");
	}

}