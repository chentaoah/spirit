package com.sum.spirit.core.visit;

import java.util.List;

import com.sum.spirit.pojo.element.impl.Statement;
import com.sum.spirit.pojo.element.impl.Token;

public class StmtVisiter extends Visiter<Statement, Token> {

	@Override
	public List<Token> getListable(Statement listable, Consumer<VisitEvent<Token>> consumer, VisitEvent<Token> event) {
		return listable.tokens;
	}

	@Override
	public Statement getSubListable(VisitEvent<Token> event) {
		Token token = event.item;
		return token.canSplit() ? token.getValue() : null;
	}

}
