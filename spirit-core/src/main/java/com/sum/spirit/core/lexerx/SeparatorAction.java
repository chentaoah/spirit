package com.sum.spirit.core.lexerx;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SeparatorAction extends AbsLexerAction {

	@Override
	public boolean isTrigger(char c) {
		return c == '"' || c == '\'' || c == '{' || c == '(' || c == '[' || c == '<';
	}

	@Override
	public void pushStack(LexerEvent event) {

		StringBuilder builder = event.builder;
		AtomicInteger index = event.index;
		char c = event.c;
		AtomicInteger count = event.count;
		AtomicInteger start = event.start;
		AtomicInteger end = event.end;
		Map<String, String> replacedStrs = event.replacedStrs;
		List<Character> ignoreChars = event.ignoreChars;

		if (c == '"') {
			pushStack(builder, index.get(), '"', '"', "@str" + count.getAndIncrement(), replacedStrs);

		} else if (c == '\'') {
			pushStack(builder, index.get(), '\'', '\'', "@char" + count.getAndIncrement(), replacedStrs);

		} else if (c == '{') {
			pushStack(builder, index.get(), '{', '}', "@map" + count.getAndIncrement(), replacedStrs);

		} else if (c == '(') {
			int idx = start.get() >= 0 ? start.get() : index.get();
			pushStack(builder, idx, '(', ')', "@invoke_like" + count.getAndIncrement(), replacedStrs);
			index.set(idx);

		} else if (c == '[') {
			if (ignoreChars.contains('{') && index.get() > end.get()) {// 一般来说，Java中没有泛型数组的声明方式
				int idx = start.get() >= 0 ? start.get() : index.get();
				pushStack(builder, idx, '[', ']', "@array_like" + count.getAndIncrement(), replacedStrs);
				index.set(idx);

			} else {
				int idx = start.get() >= 0 ? start.get() : index.get();
				pushStack(builder, idx, '[', ']', '{', '}', "@array_like" + count.getAndIncrement(), replacedStrs);
				index.set(idx);
			}

		} else if (c == '<') {
			if (start.get() >= 0) {
				char d = builder.charAt(start.get());
				if (d >= 'A' && d <= 'Z') {// 一般泛型声明都是以大写字母开头的
					if (ignoreChars.contains('(') && index.get() > end.get()) {
						pushStack(builder, start.get(), '<', '>', "@generic" + count.getAndIncrement(), replacedStrs);
						index.set(start.get());

					} else {
						pushStack(builder, start.get(), '<', '>', '(', ')', "@generic" + count.getAndIncrement(), replacedStrs);
						index.set(start.get());
					}
				}
			}
		}
	}

}
