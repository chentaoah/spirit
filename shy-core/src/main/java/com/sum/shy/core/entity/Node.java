package com.sum.shy.core.entity;

public class Node {

	public Token token;

	public Node left;

	public Node right;

	public Node(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		Stmt stmt = token.getStmt();
		return "" + (left == null ? "" : left) + stmt.format(token) + (right == null ? "" : right);
	}

}
