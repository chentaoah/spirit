package com.sum.shy.element;

import java.util.List;

import com.sum.shy.common.Constants;
import com.sum.shy.common.KeywordTable;

public class SyntaxTree {

	public List<Token> tokens;

	public SyntaxTree(List<Token> tokens) {
		this.tokens = tokens;
		markTreeId(tokens);
	}

	public void markTreeId(List<Token> tokens) {
		int count = 0;
		for (Token token : tokens) {
			if (token.isNode()) {
				markTreeId(count + "", token.getNode());
				count++;
			}
		}
	}

	public void markTreeId(String treeId, Node node) {
		// 注意：这里操作树节点token的attachments,实际上也是操作原来的token
		node.token.setTreeId(treeId);
		if (node.left != null)
			markTreeId(treeId + "-" + "0", node.left);
		if (node.right != null)
			markTreeId(treeId + "-" + "1", node.right);
		// 子语句则重新计数
		if (node.token.canVisit())
			markTreeId(node.token.getStmt().tokens);
	}

	public String getSyntax() {
		try {
			// 第一个单词
			Token first = tokens.get(0);

			// 关键字语句
			if (KeywordTable.isLine(first.toString()))
				return first.toString();

			// 本地方法调用
			if (tokens.size() == 1 && first.isLocalMethod()) {// 调用本地方法
				if (Constants.SUPER_KEYWORD.equals(first.getMemberNameAtt())) {
					return Constants.SUPER_SYNTAX;

				} else if (Constants.THIS_KEYWORD.equals(first.getMemberNameAtt())) {
					return Constants.THIS_SYNTAX;
				}

				return Constants.INVOKE_SYNTAX;
			}

			// 聚合以后的抽象语法树
			if (tokens.size() == 1 && first.isNode()) {
				Node node = first.getNode();
				Token token = node.token;
				if (token.isType()) {// 如果顶点是类型
					Token rightToken = node.right.token;
					if (rightToken.isVar()) { // String text
						return Constants.DECLARE_SYNTAX;

					} else if (rightToken.isLocalMethod()) { // String test()
						return Constants.FUNC_DECLARE_SYNTAX;
					}

				} else if (token.isAssign()) {// 如果顶点是=
					Token leftToken = node.left.token;
					if (leftToken.isType()) {// 声明并且赋值 String text = "abc"
						return Constants.DECLARE_ASSIGN_SYNTAX;

					} else if (leftToken.isVar()) {// 如果是变量,则为赋值语句 text = "abc"
						return Constants.ASSIGN_SYNTAX;

					} else if (leftToken.isVisitField()) {// 如果是字段访问,则是字段赋值语句 var.text = "abc"
						return Constants.FIELD_ASSIGN_SYNTAX;
					}

				} else if (token.isInvokeMethod()) {// 如果顶点是方法调用 list.get(0)
					return Constants.INVOKE_SYNTAX;
				}

				return Constants.INVOKE_SYNTAX;
			}

			// 第二个单词
			Token second = tokens.get(1);
			// 第三个单词
			Token third = tokens.get(2);
			if (Constants.FOR_KEYWORD.equals(first.toString())) {
				if (Constants.IN_KEYWORD.equals(third.toString())) {
					return Constants.FOR_IN_SYNTAX;
				}
				return Constants.FOR_SYNTAX;
			}
			if ("}".equals(first.toString())) {// else if语句
				if (Constants.ELSE_KEYWORD.equals(second.toString())) {
					if (Constants.IF_KEYWORD.equals(third.toString())) {
						return Constants.ELSEIF_SYNTAX;
					} else {
						return Constants.ELSE_SYNTAX;
					}

				} else if (Constants.CATCH_KEYWORD.equals(second.toString())) {
					return Constants.CATCH_SYNTAX;

				} else if (Constants.FINALLY_KEYWORD.equals(second.toString())) {
					return Constants.FINALLY_SYNTAX;
				}
			}

		} catch (Exception e) {
			// ignore
		}

		throw new RuntimeException("Unknown syntax!");

	}

}
