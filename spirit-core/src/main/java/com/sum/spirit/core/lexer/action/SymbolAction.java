package com.sum.spirit.core.lexer.action;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.enums.SymbolEnum;

@Component
@Order(-80)
public class SymbolAction extends AbsLexerAction {

	@Override
	public boolean isTrigger(LexerEvent event) {
		return SymbolEnum.isSymbolChar(event.c);
	}

	@Override
	public void pushStack(LexerEvent event) {

		StringBuilder builder = event.builder;
		AtomicInteger index = event.index;
		AtomicInteger count = event.count;
		Map<String, String> replacedStrs = event.replacedStrs;

		// 尝试获取两个字符，判断是否双字符符号
		if (index.get() + 1 < builder.length()) {
			String str = builder.substring(index.get(), index.get() + 2);
			if (SymbolEnum.isDoubleSymbol(str)) {
				replaceStr(builder, index.get(), index.get() + 1, "@symbol" + count.getAndIncrement(), replacedStrs);
				return;
			}
		}

		// 尝试获取一个字符，判断是否双字符符号
		String str = builder.substring(index.get(), index.get() + 1);
		if (SymbolEnum.isSingleSymbol(str)) {
			replaceStr(builder, index.get(), index.get(), "@symbol" + count.getAndIncrement(), replacedStrs);
			return;
		}

		throw new RuntimeException("Symbol that cannot be processed!");
	}

}