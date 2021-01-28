package com.sum.spirit.core.lexer.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LexerContext {
	public StringBuilder builder;
	public List<Character> ignoreChars;
	public AtomicInteger index = new AtomicInteger(0);
	public AtomicInteger count = new AtomicInteger(0);
	public AtomicInteger start = new AtomicInteger(-1);
	public AtomicInteger end = new AtomicInteger(-1);
	@NonNull
	public Map<String, String> replacedStrs = new HashMap<>();

	public LexerContext(StringBuilder builder, List<Character> ignoreChars) {
		this.builder = builder;
		this.ignoreChars = ignoreChars;
	}
}
