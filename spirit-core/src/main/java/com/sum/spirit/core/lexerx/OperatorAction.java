package com.sum.spirit.core.lexerx;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OperatorAction implements LexerAction {

	@Override
	public boolean isTrigger(char c) {
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

	}

}
