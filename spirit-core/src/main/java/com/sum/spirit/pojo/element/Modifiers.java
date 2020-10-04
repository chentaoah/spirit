package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.pojo.common.KeywordEnum;

public class Modifiers extends TokenBox {

	public List<Token> tokens;

	public Modifiers(List<Token> tokens) {
		List<Token> modifiers = new ArrayList<>();
		Iterator<Token> iterable = tokens.iterator();
		while (iterable.hasNext()) {
			Token token = iterable.next();
			if (KeywordEnum.isModifier(token.toString())) {
				modifiers.add(token);
				iterable.remove();
			} else {
				break;
			}
		}
		this.tokens = modifiers;
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
