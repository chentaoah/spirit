package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;
import com.sum.shy.element.Tree;

@Service("tree_builder")
public interface TreeBuilder {

	Tree build(Stmt stmt);

	List<Token> build(List<Token> tokens);

}
