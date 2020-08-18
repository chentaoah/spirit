package com.sum.soon.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.element.Statement;
import com.sum.soon.pojo.element.SyntaxTree;
import com.sum.soon.pojo.element.Token;

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
