package com.sum.spirit.utils;

import java.util.List;
import java.util.Map;

import com.sum.spirit.pojo.element.impl.Statement;
import com.sum.spirit.pojo.element.impl.Token;

public class StmtVisiter extends Visiter<Statement, Token> {

	@Override
	public List<Token> getListable(Statement listable, Map<String, Object> context) {
		return listable.tokens;
	}

	@Override
	public Statement getSubListable(VisitEvent<Token> event) {
		Token token = event.item;
		return token.canSplit() ? token.getValue() : null;
	}

}
