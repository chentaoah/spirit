package com.sum.spirit.core.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LexerResult {

	public enum State {
		TRY, SKIP
	}

	public State state;
	public Region region;

}
