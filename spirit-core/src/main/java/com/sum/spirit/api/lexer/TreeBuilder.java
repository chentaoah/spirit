package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.AbstractSyntaxTree;
import com.sum.spirit.pojo.element.Token;

@Service("tree_builder")
public interface TreeBuilder {

	default AbstractSyntaxTree build(Statement statement) {

		List<Token> tokens = build(statement.tokens);

		markTreeId(tokens);

		return new AbstractSyntaxTree(tokens);

	}

	List<Token> build(List<Token> tokens);

	void markTreeId(List<Token> tokens);

}
