package com.sum.spirit.lexer.entity;

import java.util.Arrays;
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
	public List<Character> splitChars;

	public int nameCount;
	@NonNull
	public Map<String, String> replacedStrs = new HashMap<>();

	public int startIndex = -1;
	public int index;

	public Map<String, Object> attachments = new HashMap<>();

	public LexerContext(StringBuilder builder, Character... splitChars) {
		this.builder = builder;
		this.splitChars = Arrays.asList(splitChars);
	}

}
