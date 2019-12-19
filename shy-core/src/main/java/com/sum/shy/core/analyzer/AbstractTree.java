package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.LineUtils;

/**
 * 抽象语法树
 * 
 * @author chentao26275
 *
 */
public class AbstractTree {
	// "++", "--", "!",
	public static final String[] OPERATORS = new String[] { "*", "/", "%", "+", "-", "==", "!=", "<", ">", "<=", ">=",
			"&&", "||", };

	public static final int[] OPERATOR_PRIORITY = new int[] { 35, 30, 30, 30, 25, 25, 20, 20, 20, 20, 20, 20, 15, 15 };

	public static final String[] KEYWORDS = new String[] { "instanceof" };

	public static final int[] KEYWORD_PRIORITY = new int[] { 20 };

	/**
	 * 语法树,并不能处理所有的语句,比如for循环语句,只能处理一个简单的表达式
	 * 
	 * @param stmt
	 * @return
	 */
	public static Node grow(Stmt stmt) {
		// 如果只有一个元素
		if (stmt.size() == 1)
			return new Node(stmt.getToken(0));

		// 1.为每个操作符,或者特殊的关键字,进行优先级分配
		int maxPriority = -1;// 优先级
		int index = -1;
		Token currToken = null;// 当前优先级最高的操作符

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			// 如果是操作符
			if (token.isOperator() || (token.isKeyword() && token.isInstanceof())) {
				int priority = getPriority(token.value.toString());
				if (priority > maxPriority) {
					maxPriority = priority;
					index = i;
					currToken = token;
				}
			}
		}
		if (index >= 0) {
			// 将操作符左右的元素,组成节点
			int last = index - 1;
			int next = index + 1;

			Node node = new Node(currToken);
			Token lastToken = last >= 0 ? stmt.getToken(last) : null;
			Token nextToken = next < stmt.size() ? stmt.getToken(next) : null;
			if (lastToken != null)
				node.left = lastToken instanceof Node ? (Node) lastToken : new Node(lastToken);
			if (nextToken != null)
				node.right = nextToken instanceof Node ? (Node) nextToken : new Node(nextToken);
			// 替换
			stmt = stmt.replace(last >= 0 ? last : 0, next < stmt.size() ? next + 1 : stmt.size(), node);

			// 递归
			return grow(stmt);

		}

		return null;
	}

	public static int getPriority(String value) {
		int count = 0;
		for (String operator : OPERATORS) {
			if (operator.equals(value)) {
				return OPERATOR_PRIORITY[count];
			}
			count++;
		}
		count = 0;
		for (String operator : KEYWORDS) {
			if (operator.equals(value)) {
				return KEYWORD_PRIORITY[count];
			}
			count++;
		}
		return -1;
	}

	public static class Node extends Token {

		public Token token;

		public Node left;

		public Node right;

		public Node(Token content) {
			this.token = content;
		}

	}

	/**
	 * 测试案例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String text = "(x > 0 || y < 100) && list.size()>100 && obj instanceof Object";
		Stmt stmt = Stmt.create(text);
		Node node = grow(stmt);
		System.out.println(buildTree(node));
	}

	private static String buildTree(Node node) {
		// 最上面那个节点的缩进
		int indent = getIndent(node);
		// 按照缩进打印节点
		StringBuilder sb = new StringBuilder();
		sb.append(LineUtils.getSpaceByNumber(indent * 4) + node.token.value.toString());

		return "";
	}

	private static int getIndent(Node node) {
		// 最大缩进
		int maxIndent = -1;
		// 起点的缩进量
		int startIndent = 0;
		// 起始节点
		Node startNode = node;
		while (startNode != null) {
			int indent = 0;
			// 向左
			while (node.left != null) {
				indent++;
				node = node.left;
			}
			if (startIndent + indent > maxIndent) {
				maxIndent = startIndent + indent;
			}
			startNode = startNode.right;
			startIndent--;
		}

		return maxIndent;
	}

}
