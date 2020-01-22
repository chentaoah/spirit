package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Node {

	public static final String[] BINARY_OPERATOR = new String[] { "+", "-", "*", "/", "%", "==", "!=", "<", ">", "<=",
			">=", "&&", "||", "<<", ">>" };

	public Token token;

	public Node left;

	public Node right;

	public Node(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Node> nodes = getNodes();
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			sb.append(node.format());
		}
		return sb.toString().trim();
	}

	public String format() {
		if (token.isOperator()) {
			String value = token.toString();
			if ("++".equals(value) || "--".equals(value)) {// 一元操作符
				if (left != null)
					return value + " ";
				if (right != null)
					return " " + value;
			} else if ("!".equals(value)) {
				return value;
			}
			for (String operator : BINARY_OPERATOR) {// 二元操作符
				if (operator.equals(value))
					return " " + value + " ";
			}

		} else if (token.isCast()) {// 类型转换
			return token + " ";

		} else if (token.isInstanceof()) {// 关键字
			return " " + token + " ";
		}
		// 其他
		return token.toString();
	}

	public Stmt toStmt() {
		return new Stmt(getTokens());
	}

	public List<Token> getTokens() {
		List<Token> tokens = new ArrayList<>();
		tokens.add(token);
		if (left != null)
			tokens.addAll(0, left.getTokens());
		if (right != null)
			tokens.addAll(right.getTokens());
		return tokens;
	}

	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<>();
		nodes.add(this);
		if (left != null)
			nodes.addAll(0, left.getNodes());
		if (right != null)
			nodes.addAll(right.getNodes());
		return nodes;
	}

}
