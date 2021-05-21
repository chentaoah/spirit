package com.sum.spirit.core.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LexerResult extends CommonResult {

	public enum State {
		CONTINUE, BREAK
	}

	public State state;

	public LexerResult(State state, Object value) {
		super(value);
		this.state = state;
	}

}
