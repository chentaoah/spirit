package com.sum.spirit.core.element.action.lexer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LexerEvent {
	public LexerContext context;
	public char c;
}
