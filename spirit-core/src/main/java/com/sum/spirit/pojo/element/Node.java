package com.sum.spirit.pojo.element;

public class Node {

	public int index;

	public Token token;

	public Node prev;

	public Node next;

	public Node(int index, Token token) {
		this.index = index;
		this.token = token;
	}

	public boolean isDirty() {
		return prev != null || next != null;
	}

	public boolean canSplit() {
		return token != null && token.value != null && token.value instanceof AbsSyntaxTree;
	}

	@Override
	public String toString() {
		return token.toString();
	}

}
