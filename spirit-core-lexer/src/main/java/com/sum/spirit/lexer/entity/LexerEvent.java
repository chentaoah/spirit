package com.sum.spirit.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LexerEvent {
	public LexerContext context;
	public char ch;
}
