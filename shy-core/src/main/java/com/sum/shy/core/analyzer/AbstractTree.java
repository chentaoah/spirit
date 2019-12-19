package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

/**
 * 抽象语法树
 * 
 * @author chentao26275
 *
 */
public class AbstractTree {
	// "++", "--",
	public static final String[] OPERATORS = new String[] { "!", "*", "/", "%", "+", "-", "==", "!=", "<", ">", "<=",
			">=", "&&", "||", };

	public static final int[] OPERATOR_PRIORITY = new int[] { 35, 30, 30, 30, 25, 25, 20, 20, 20, 20, 20, 20, 15, 15 };

	public static final String[] KEYWORDS = new String[] { "instanceof" };

	public static final int[] KEYWORD_PRIORITY = new int[] { 20 };

	/**
	 * 语法树,并不能处理所有的语句,比如for循环语句,只能处理一个简单的表达式
	 * 
	 * @param stmt
	 * @return
	 */
	public static Node<Token> grow(Stmt stmt) {
		// 1.为每个操作符,或者特殊的关键字,进行优先级分配
		Node<Token> node = new Node<Token>();
		int maxPriority = -1;// 优先级
		Token currToken = null;// 当前优先级最高的操作符
		int index = -1;
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			// 如果是操作符
			if (token.isOperator() || (token.isKeyword() && token.isInstanceof())) {
				int priority = getPriority(token.value.toString());
				if (priority > maxPriority) {
					maxPriority = priority;
					currToken = token;
					index = i;
				}
			}
		}
		if (index >= 0) {
			// 将操作符左右的元素,组成节点
			node.content = currToken;
			
		}

		return node;
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

	public static class Node<T> {

		public T content;

		public Node<T> left;

		public Node<T> right;

	}
}
