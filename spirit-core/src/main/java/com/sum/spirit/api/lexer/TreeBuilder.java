package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.SyntaxTree;
import com.sum.spirit.pojo.element.Token;

@Service("tree_builder")
public interface TreeBuilder {

	default SyntaxTree build(Statement statement) {

		List<Token> tokens = build(statement.tokens);

		markTreeId(tokens);

		return new SyntaxTree(tokens);

	}

	List<Token> build(List<Token> tokens);

	void markTreeId(List<Token> tokens);

}
