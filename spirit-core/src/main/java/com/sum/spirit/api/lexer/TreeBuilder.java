package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.AbsSyntaxTree;
import com.sum.spirit.pojo.element.Token;

public interface TreeBuilder {

	default AbsSyntaxTree build(Statement statement) {
		List<Token> tokens = build(statement.tokens);
		markTreeId(tokens);
		return new AbsSyntaxTree(tokens);
	}

	List<Token> build(List<Token> tokens);

	void markTreeId(List<Token> tokens);

}
