package com.sum.spirit.core.element.handler;

import java.util.List;

import com.sum.spirit.core.common.enums.AttributeEnum;
import com.sum.spirit.core.element.entity.Node;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;

public abstract class AbstractTreeBuilder {

	public SyntaxTree buildTree(Statement statement) {
		// 用语句构建节点树
		List<Node> nodes = buildNodes(statement.tokens);
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
		node.token.setAttr(AttributeEnum.TREE_ID, treeId);
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
		List<Token> tokens = statement.format();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			token.setAttr(AttributeEnum.POSITION, position);
			if (token.canSplit()) {
				markPositionAndLength(position, token.getValue());
			}
			int length = token.toString().length();
			token.setAttr(AttributeEnum.LENGTH, length);
			position += length;
		}
	}

	protected abstract List<Node> buildNodes(List<Token> tokens);

}
