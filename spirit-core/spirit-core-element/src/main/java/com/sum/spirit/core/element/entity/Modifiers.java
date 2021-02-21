package com.sum.spirit.core.element.entity;

import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.common.utils.HeadVisiter;
import com.sum.spirit.core.element.frame.KeywordTokenBox;

public class Modifiers extends KeywordTokenBox {

	public Modifiers(List<Token> tokens) {
		super(new HeadVisiter<Token>().visit(tokens, token -> token.isModifier()));
	}

	@Override
	public String toString() {
		return Joiner.on(" ").join(this);
	}

}
