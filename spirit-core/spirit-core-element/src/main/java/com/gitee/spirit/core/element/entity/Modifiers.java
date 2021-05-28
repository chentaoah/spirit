package com.gitee.spirit.core.element.entity;

import java.util.List;

import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.element.frame.KeywordTokenBox;
import com.google.common.base.Joiner;

public class Modifiers extends KeywordTokenBox {

	public Modifiers(List<Token> tokens) {
		super(ListUtils.filterStoppable(tokens, token -> token.isModifier()));
	}

	@Override
	public String toString() {
		return Joiner.on(" ").join(this);
	}

}
