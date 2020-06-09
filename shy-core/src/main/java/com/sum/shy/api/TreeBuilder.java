package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.element.SyntaxTree;

@Service("tree_builder")
public interface TreeBuilder {

	SyntaxTree build(Statement stmt);

	List<Token> build(List<Token> tokens);

}
