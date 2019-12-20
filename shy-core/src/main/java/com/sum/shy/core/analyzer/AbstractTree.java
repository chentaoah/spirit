package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

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
		// 为每个token计算位置
		int position = 0;
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			// 格式化的长度
			String text = stmt.getTokenStr(i, token);
			// 先使用位置,再将自己的长度追加到位置中
			token.setPosition(position + (text.startsWith(" ") ? 1 : 0));
			// 加上当前的长度
			position += text.length();
		}
		return getNodeByLoop(stmt);
	}

	private static Node getNodeByLoop(Stmt stmt) {

		// 如果只有一个元素
		if (stmt.size() == 1) {
			Token token = stmt.getToken(0);
			return token.isNode() ? (Node) token.value : new Node(token);
		}

		// 1.为每个操作符,或者特殊的关键字,进行优先级分配
		int maxPriority = -1;// 优先级
		Token lastToken = null;
		Token currToken = null;// 当前优先级最高的操作符
		Token nextToken = null;
		int index = -1;

		// 每个token在一行里面的位置
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
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

		Node node = new Node(currToken);
		if (lastToken != null)
			node.left = lastToken.isNode() ? (Node) lastToken.value : new Node(lastToken);
		if (nextToken != null)
			node.right = nextToken.isNode() ? (Node) nextToken.value : new Node(nextToken);

		// 替换
		stmt = stmt.replace(index - 1 >= 0 ? index - 1 : 0, index + 1 < stmt.size() ? index + 1 + 1 : stmt.size(),
				new Token(Constants.NODE_TOKEN, node, null));

		// 递归
		return getNodeByLoop(stmt);

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

		public Token token;

		public Node left;

		public Node right;

		public Node(Token token) {
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
		for (int i = 0; i < 20; i++) {
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
			if (!line.isIgnore()) {
				System.out.println(line.text);
			}
		}
	}

	private static void buildTree(List<Line> lines, int depth, String separator, Node node) {

		if (node == null)
			return;
		// 节点内容
		String text = node.token.value.toString();
		// 获取上一行
		if (StringUtils.isNotEmpty(separator)) {
			Line lastLine = lines.get(depth - 1);
			StringBuilder sb = new StringBuilder(lastLine.text);
			// 尽量上上面的分割符在中间
			int position = node.token.getPosition() + text.length() / 2;
			sb.replace(position, position + 1, separator);
			lastLine.text = sb.toString();
		}
		// 在节点的上方
		Line line = lines.get(depth);
		StringBuilder sb = new StringBuilder(line.text);
		int position = node.token.getPosition();
		sb.replace(position, position + text.length(), text);
		line.text = sb.toString();

		// 左边节点
		buildTree(lines, depth + 2, "/", node.left);
		// 右边节点
		buildTree(lines, depth + 2, "\\", node.right);

	}

}
