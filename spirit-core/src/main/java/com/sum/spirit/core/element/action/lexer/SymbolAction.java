package com.sum.spirit.core.element.action.lexer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.common.enums.SymbolEnum;

@Component
@Order(-80)
public class SymbolAction extends AbstractLexerAction {

	@Override
	public boolean isTrigger(LexerEvent event) {
		return SymbolEnum.isSymbolChar(event.c);
	}

	@Override
	public void pushStack(LexerEvent event) {

		StringBuilder builder = event.context.builder;
		AtomicInteger index = event.context.index;
		AtomicInteger count = event.context.count;
		Map<String, String> replacedStrs = event.context.replacedStrs;

		// 尝试获取两个字符，判断是否双字符符号
		if (index.get() + 1 < builder.length()) {
			String str = builder.substring(index.get(), index.get() + 2);
			if (SymbolEnum.isDoubleSymbol(str)) {
				replaceStr(builder, index.get(), index.get() + 2, "@symbol" + count.getAndIncrement(), replacedStrs);
				return;
			}
		}

		// 尝试获取一个字符，判断是否双字符符号
		String str = builder.substring(index.get(), index.get() + 1);
		if (SymbolEnum.isSingleSymbol(str)) {
			replaceStr(builder, index.get(), index.get() + 1, "@symbol" + count.getAndIncrement(), replacedStrs);
			return;
		}

		throw new RuntimeException("Symbol that cannot be processed!");
	}

}