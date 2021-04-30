package com.sum.spirit.core.lexer.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LexerContext extends CharsContext {

	public int startIndex = -1;
	public List<Region> regions = new ArrayList<>();
	public String profile;
	public List<Character> splitChars;
	public List<String> words;

	public LexerContext(StringBuilder builder) {
		this.builder = builder;
	}

	public LexerContext(StringBuilder builder, String profile, Character... splitChars) {
		this.builder = builder;
		this.profile = profile;
		this.splitChars = Arrays.asList(splitChars);
	}

}
