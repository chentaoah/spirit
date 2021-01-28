package com.sum.spirit.pojo.element.impl;

import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.core.visit.HeadVisiter;
import com.sum.spirit.pojo.element.api.TokenBox;

public class Modifiers extends TokenBox {

	public Modifiers(List<Token> tokens) {
		super(new HeadVisiter<Token>().visit(tokens, token -> token.isModifier()));
	}

	@Override
	public String toString() {
		return Joiner.on(" ").join(tokens);
	}

}
