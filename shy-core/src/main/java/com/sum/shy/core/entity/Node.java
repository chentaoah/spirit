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
		String text = null;
		if (token.isExpress()) {// 如果是手动添加的表达式,那么则直接用表达式来拼接字符串
			text = token.value.toString();

		} else {
			Stmt stmt = token.getStmt();
			text = stmt.format(token);
		}
		return "" + (left == null ? "" : left) + text + (right == null ? "" : right);
	}

}
