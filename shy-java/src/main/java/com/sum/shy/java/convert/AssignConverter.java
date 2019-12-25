package com.sum.shy.java.convert;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.AbsSyntaxTree;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

public class AssignConverter extends DefaultConverter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {

		// 保留第一个var token
		Token token = stmt.getToken(0);

		// 查找==节点
		if (line.text.contains("==")) {
			Node node = AbsSyntaxTree.grow(stmt);
			List<Node> nodes = new ArrayList<>();
			findEquals(node, nodes);
			for (Node someNode : nodes) {
				String express = String.format("StringUtils.equals(%s, %s)", someNode.left, someNode.right);
				someNode.token = new Token(Constants.EXPRESS_TOKEN, express, null);
				someNode.left = null;
				someNode.right = null;
			}
			// 合成一个新的语句
			stmt = Stmt.create(node.toString());
		}
		// 一般的转换
		stmt = convertStmt(clazz, stmt);

		if (token.isVar() && !token.isDeclaredAtt()) {
			stmt.tokens.add(0, new Token(Constants.TYPE_TOKEN, token.getTypeAtt(), null));
		}
		return stmt;

	}

	private void findEquals(Node node, List<Node> nodes) {
		// 如果当前节点就是
		Token token = node.token;
		if (token.isOperator() && "==".equals(token.value)) {
			// 如果两边节点都是str的话
			if (node.left.token.getTypeAtt().isStr() && node.right.token.getTypeAtt().isStr()) {
				nodes.add(node);
			}
		}
		// 查找子节点
		if (node.left != null)
			findEquals(node.left, nodes);
		if (node.right != null)
			findEquals(node.right, nodes);

	}

}
