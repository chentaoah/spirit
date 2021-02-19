package com.sum.spirit.core.api;

import java.util.List;

import com.sum.spirit.core.element.entity.Node;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;

public interface TreeBuilder {

	SyntaxTree buildTree(Statement statement);

	List<Node> buildNodes(List<Token> tokens);

}
