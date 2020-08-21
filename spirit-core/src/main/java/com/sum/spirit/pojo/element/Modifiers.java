package com.sum.spirit.pojo.element;

import java.util.List;

import com.google.common.base.Joiner;

public class Modifiers extends TokenBox {

	public List<Token> tokens;

	public Modifiers(List<Token> tokens) {
		this.tokens = tokens;
	}

	@Override
	public List<Token> getTokens() {
		return tokens;
	}

	@Override
	public String toString() {
		return Joiner.on(" ").join(tokens);
	}

}
