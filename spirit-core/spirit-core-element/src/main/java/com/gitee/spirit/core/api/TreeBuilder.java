package com.gitee.spirit.core.api;

import java.util.List;

import com.gitee.spirit.core.element.entity.Node;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.SyntaxTree;
import com.gitee.spirit.core.element.entity.Token;

public interface TreeBuilder {

	SyntaxTree buildTree(Statement statement);

	List<Node> buildNodes(List<Token> tokens);

}
