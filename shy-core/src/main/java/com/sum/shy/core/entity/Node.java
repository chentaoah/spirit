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
		return "" + (left == null ? "" : left) + token + (right == null ? "" : right);
	}

}
