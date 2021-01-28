package com.sum.spirit.core.element.pojo;

import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.core.element.pojo.api.TokenBox;
import com.sum.spirit.core.visiter.utils.HeadVisiter;

public class Modifiers extends TokenBox {

	public Modifiers(List<Token> tokens) {
		super(new HeadVisiter<Token>().visit(tokens, token -> token.isModifier()));
	}

	@Override
	public String toString() {
		return Joiner.on(" ").join(tokens);
	}

}
