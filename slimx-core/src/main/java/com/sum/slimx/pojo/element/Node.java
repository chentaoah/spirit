package com.sum.slimx.pojo.element;

public class Node {

	public Token token;

	public Node left;

	public Node right;

	public Node(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return token.toString();
	}

}
