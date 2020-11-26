package com.sum.spirit.plug.test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.sum.spirit.core.lexer.Lexer;
import com.sum.spirit.pojo.lexer.LexerEvent;

public class FlowLexer extends Lexer {

	@Override
	public boolean isTrigger(LexerEvent event) {
		char c = event.c;
		return c == '-' || c == '|' || c == '[' || c == '(' || c == '{';
	}

	@Override
	public void pushStack(LexerEvent event) {

		StringBuilder builder = event.builder;
		AtomicInteger index = event.index;
		char c = event.c;
		AtomicInteger count = event.count;
		AtomicInteger start = event.start;
		Map<String, String> replacedStrs = event.replacedStrs;

		if (c == '-') {
			replaceStr(builder, index.get(), index.get() + 2, "@symbol" + count.getAndIncrement(), replacedStrs);

		} else if (c == '|') {
			pushStack(builder, index.get(), '|', '|', "@condition" + count.getAndIncrement(), replacedStrs);

		} else if (c == '[') {
			pushStack(builder, start.get(), '[', ']', "@component" + count.getAndIncrement(), replacedStrs);
			index.set(start.get());

		} else if (c == '(') {
			pushStack(builder, start.get(), '(', ')', "@component" + count.getAndIncrement(), replacedStrs);
			index.set(start.get());

		} else if (c == '{') {
			pushStack(builder, start.get(), '{', '}', "@component" + count.getAndIncrement(), replacedStrs);
			index.set(start.get());
		}
	}

}
