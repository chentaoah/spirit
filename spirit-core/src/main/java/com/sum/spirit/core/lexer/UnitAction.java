package com.sum.spirit.core.lexer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.lexer.LexerEvent;
import com.sum.spirit.utils.LineUtils;

@Component
@Order(-100)
public class UnitAction extends AbsLexerAction {

	@Override
	public boolean isTrigger(LexerEvent event) {

		StringBuilder builder = event.builder;
		AtomicInteger index = event.index;
		char c = event.c;
		AtomicInteger start = event.start;
		AtomicInteger end = event.end;
		List<Character> ignoreChars = event.ignoreChars;

		// 是否忽略该字符
		if (ignoreChars.contains(c) && index.get() > end.get()) {
			start.set(-1);
			end.set(findEnd(builder, index.get(), c, LineUtils.flipChar(c)));
			ignoreChars.remove(new Character(c));
			return false;
		}

		if (index.get() == builder.length() - 1) {
			return false;
		}

		if (c == '"' || c == '\'' || c == '{' || c == '(' || c == '[') {
			return true;

		} else if (c == '<') {
			if (start.get() >= 0) {
				char d = event.builder.charAt(start.get());
				if (d >= 'A' && d <= 'Z') {// 一般泛型声明都是以大写字母开头的
					return true;
				}
			}
		}

		return false;
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
