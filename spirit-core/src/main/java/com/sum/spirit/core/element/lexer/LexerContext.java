package com.sum.spirit.core.element.lexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public int nameCount;
	@NonNull
	public Map<String, String> replacedStrs = new HashMap<>();

	public int index;
	public int startIndex = -1;
	public int endIndex = -1;

	public LexerContext(StringBuilder builder, List<Character> ignoreChars) {
		this.builder = builder;
		this.ignoreChars = ignoreChars;
	}
}
