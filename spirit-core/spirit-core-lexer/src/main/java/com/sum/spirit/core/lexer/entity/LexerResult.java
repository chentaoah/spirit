package com.sum.spirit.core.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LexerResult {

	public enum State {
		CONTINUE, BREAK
	}

	public State state;
	public Region region;

}
