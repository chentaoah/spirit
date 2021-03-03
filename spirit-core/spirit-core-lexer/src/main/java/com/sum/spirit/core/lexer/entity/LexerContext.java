package com.sum.spirit.core.lexer.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
public class LexerContext extends CharsContext {

	public List<Character> splitChars;
	public int nameCount;
	@NonNull
	public Map<String, String> replacedStrs = new HashMap<>();
	public int startIndex = -1;
	public List<String> words;

	public LexerContext(StringBuilder builder, Character... splitChars) {
		this.builder = builder;
		this.splitChars = Arrays.asList(splitChars);
	}

}
