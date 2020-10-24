package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;

public class Modifiers extends TokenBox {

	public List<Token> tokens = new ArrayList<>();

	public Modifiers(List<Token> tokens) {
		Iterator<Token> iterable = tokens.iterator();
		while (iterable.hasNext()) {
			Token token = iterable.next();
			if (token.isModifier()) {
				this.tokens.add(token);
				iterable.remove();
				continue;
			}
			break;
		}
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
