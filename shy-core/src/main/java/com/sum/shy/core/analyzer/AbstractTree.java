package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.LineUtils;
import com.sum.shy.lib.StringUtils;

/**
 * 抽象语法树
 * 
 * @author chentao26275
 *
 */
public class AbstractTree {
	// "++", "--", "!",
	public static final String[] OPERATORS = new String[] { "*", "/", "%", "+", "-", "==", "!=", "<", ">", "<=", ">=",
			"&&", "||" };

	public static final int[] OPERATOR_PRIORITY = new int[] { 30, 30, 30, 25, 25, 20, 20, 20, 20, 20, 20, 15, 15 };

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
		if (stmt.size() == 1) {
			Token token = stmt.getToken(0);
			return token.isNode() ? (Node) token.value : new Node(0, token);
		}

		// 1.为每个操作符,或者特殊的关键字,进行优先级分配
		int maxPriority = -1;// 优先级
		Token lastToken = null;
		Token currToken = null;// 当前优先级最高的操作符
		Token nextToken = null;
		int index = -1;

		// 每个token在一行里面的位置
		Map<Token, Integer> positionMap = new LinkedHashMap<>();
		for (int i = 0, position = 0; i < stmt.size(); i++) {

			Token token = stmt.getToken(i);

			// token在实际字符串中的起始位置,+1是微调一下位置
			position += token.value.toString().length();
			positionMap.put(token, position);

			// 如果是操作符
			if (token.isOperator() || token.isInstanceof()) {
				int priority = getPriority(token.value.toString());
				if (priority > maxPriority) {
					maxPriority = priority;
					lastToken = i - 1 >= 0 ? stmt.getToken(i - 1) : null;
					currToken = token;
					nextToken = i + 1 < stmt.size() ? stmt.getToken(i + 1) : null;
					index = i;
				}
			}
		}
		// 校验
		if (currToken == null)
			return null;

		Node node = new Node(positionMap.get(currToken), currToken);
		if (lastToken != null)
			node.left = lastToken.isNode() ? (Node) lastToken.value : new Node(positionMap.get(lastToken), lastToken);
		if (nextToken != null)
			node.right = nextToken.isNode() ? (Node) nextToken.value : new Node(positionMap.get(nextToken), nextToken);
		// 替换
		stmt = stmt.replace(index - 1 >= 0 ? index - 1 : 0, index + 1 < stmt.size() ? index + 1 + 1 : stmt.size(),
				new Token(Constants.NODE_TOKEN, node, null));
		// 递归
		return grow(stmt);

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

	public static class Node {

		public int position;

		public Token token;

		public Node left;

		public Node right;

		public Node(int position, Token token) {
			this.position = position;
			this.token = token;
		}

		@Override
		public String toString() {
			return "" + (left == null ? "" : left) + " " + token.value + " " + (right == null ? "" : right);
		}

	}

	/**
	 * 测试案例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 构建一个画布
		List<Line> lines = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			lines.add(new Line(i + 1, LineUtils.getSpaceByNumber(80)));
		}

		String text = "(x > 0 || y < 100) && list.size()>100 && obj instanceof Object";

		Stmt stmt = Stmt.create(text);
		System.out.println(stmt.toString());
		Node node = grow(stmt);
		// 在20行中构建树结构
		buildTree(lines, 0, null, node);
		// 打印
		for (Line line : lines) {
			System.out.println(line.text);
		}
	}

	private static void buildTree(List<Line> lines, int depth, String separator, Node node) {

		if (node == null)
			return;

		if (StringUtils.isNotEmpty(separator)) {
			Line lastLine = lines.get(depth - 1);
			StringBuilder sb = new StringBuilder(lastLine.text);
			sb.replace(node.position, node.position + 1, separator);
			lastLine.text = sb.toString();
		}
		// 在节点的上方
		Line line = lines.get(depth);
		StringBuilder sb = new StringBuilder(line.text);
		String text = node.token.value.toString();
		sb.replace(node.position, node.position + text.length(), text);
		line.text = sb.toString();
		// 右边节点
		buildTree(lines, depth + 2, "\\", node.right);
		// 左边节点
		buildTree(lines, depth + 2, "/", node.left);

	}

}
