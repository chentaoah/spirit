package com.sum.spirit.pojo.common;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LexerEvent {

	public StringBuilder builder;

	public AtomicInteger index;

	public char c;

	public AtomicInteger count;

	public AtomicInteger start;

	public AtomicInteger end;

	public Map<String, String> replacedStrs;

	public List<Character> ignoreChars;

	public LexerEvent(StringBuilder builder, AtomicInteger index, char c, AtomicInteger count, AtomicInteger start, AtomicInteger end,
			Map<String, String> replacedStrs, List<Character> ignoreChars) {
		this.builder = builder;
		this.index = index;
		this.c = c;
		this.count = count;
		this.start = start;
		this.end = end;
		this.replacedStrs = replacedStrs;
		this.ignoreChars = ignoreChars;
	}

}
