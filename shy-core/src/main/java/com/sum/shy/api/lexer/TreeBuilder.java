package com.sum.shy.api.lexer;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.element.SyntaxTree;

@Service("tree_builder")
public interface TreeBuilder {

	default SyntaxTree build(Statement stmt) {
		return new SyntaxTree(build(stmt.tokens));
	}

	List<Token> build(List<Token> tokens);

}
