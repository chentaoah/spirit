package com.sum.shy.core.entity;

public class Node {

	public static final String[] BINARY_OPERATOR = new String[] { "+", "-", "*", "/", "%", "==", "!=", "<", ">", "<=",
			">=", "&&", "||" };

	public Token token;

	public Node left;

	public Node right;

	public Node(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "" + (left == null ? "" : left) + format() + (right == null ? "" : right);
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

}
