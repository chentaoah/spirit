package com.sum.shy.api.lexer;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.element.SyntaxTree;

@Service("tree_builder")
public interface TreeBuilder {

	default SyntaxTree build(Statement stmt) {

		List<Token> tokens = build(stmt.tokens);

		markTreeId(tokens);

		return new SyntaxTree(tokens);

	}

	List<Token> build(List<Token> tokens);

	void markTreeId(List<Token> tokens);

}
