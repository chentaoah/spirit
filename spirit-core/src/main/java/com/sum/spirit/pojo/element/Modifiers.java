package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.pojo.element.frame.TokenBox;

public class Modifiers extends TokenBox {

	public Modifiers(List<Token> tokens) {
		super(new ArrayList<>());
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
	public String toString() {
		return Joiner.on(" ").join(tokens);
	}

}
