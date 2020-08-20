package com.sum.slimx.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.element.Statement;
import com.sum.slimx.pojo.element.SyntaxTree;
import com.sum.slimx.pojo.element.Token;

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
