package com.sum.spirit.api;

import java.util.List;

import com.sum.spirit.common.entity.Node;
import com.sum.spirit.common.entity.Statement;
import com.sum.spirit.common.entity.SyntaxTree;
import com.sum.spirit.common.entity.Token;

public interface TreeBuilder {

	SyntaxTree buildTree(Statement statement);

	List<Node> buildNodes(List<Token> tokens);

}
