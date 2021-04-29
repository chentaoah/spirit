package com.sum.spirit.core.element.action;

import java.util.List;

import com.sum.spirit.common.constants.Attribute;
import com.sum.spirit.core.api.TreeBuilder;
import com.sum.spirit.core.element.entity.Node;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.element.utils.StmtFormat;

public abstract class AbstractTreeBuilder implements TreeBuilder {

	@Override
	public SyntaxTree buildTree(Statement statement) {
		// 用语句构建节点树
		List<Node> nodes = buildNodes(statement);
		// 标记树节点id
		markTreeId(nodes);
		// 标记所有的位置
		markPositionAndLength(0, statement);
		// 返回抽象语法树
		return new SyntaxTree(nodes);
	}

	public void markTreeId(List<Node> nodes) {
		int count = 0;
		for (Node node : nodes) {
			markTreeId(count++ + "", node);
		}
	}

	public void markTreeId(String treeId, Node node) {
		node.token.setAttr(Attribute.TREE_ID, treeId);
		if (node.prev != null) {
			markTreeId(treeId + "-" + "0", node.prev);
		}
		if (node.next != null) {
			markTreeId(treeId + "-" + "1", node.next);
		}
		if (node.canSplit()) {
			SyntaxTree syntaxTree = node.token.getValue();
			markTreeId(syntaxTree.nodes);
		}
	}

	public void markPositionAndLength(int position, Statement statement) {
		// 获取到插入空格后
		List<Token> tokens = StmtFormat.format(statement);
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			token.setAttr(Attribute.POSITION, position);
			if (token.canSplit()) {
				markPositionAndLength(position, token.getValue());
			}
			int length = token.toString().length();
			token.setAttr(Attribute.LENGTH, length);
			position += length;
		}
	}

}
