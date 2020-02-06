package com.sum.shy.core.doc;

import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Token;

public class Tree {
	// 关键字
	public static final String[] LINE_KEYWORDS = new String[] { "if", "while", "try", "sync", "return", "break",
			"continue", "throw", "print", "debug", "error" };

	public List<Token> tokens;

	public Tree(List<Token> tokens) {
		this.tokens = tokens;
	}

	public String getSyntax() {
		try {
			// 空校验
			if (tokens == null || tokens.size() == 0)
				return Constants.UNKNOWN;

			// 第一个单词
			Token first = tokens.get(0);
			for (String keyword : LINE_KEYWORDS) {// 关键字语句
				if (keyword.equals(first.toString()))
					return keyword;
			}
			// 注解
			if (tokens.size() == 1 && first.isAnnotation()) {
				return Constants.ANNOTATION_SYNTAX;
			}
			// 语句结束
			if (tokens.size() == 1 && "}".equals(first.toString())) {
				return Constants.END_SYNTAX;
			}
			// 本地方法调用
			if (tokens.size() == 1 && first.isInvokeLocal()) {// 调用本地方法
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
				if (token.isInvokeMethod()) {// 只有方法调用
					return Constants.INVOKE_SYNTAX;
				} else if (token.isAssign()) {// 如果顶点是=
					Token leftToken = node.left.token;
					if (leftToken.isVar()) {// 如果是变量,则为赋值语句
						return Constants.ASSIGN_SYNTAX;
					} else if (leftToken.isVisitField()) {// 如果是字段访问,则是字段赋值语句
						return Constants.FIELD_ASSIGN_SYNTAX;
					}
				}
				return Constants.INVOKE_SYNTAX;
			}

			// 第二个单词
			Token second = tokens.get(1);
			if (tokens.size() == 2 && first.isType() && second.isVar()) {// 如果是类型,则是类型说明语句
				return Constants.DECLARE_SYNTAX;
			}
			if (tokens.size() == 2 && first.isType() && second.isInvokeLocal()) {// 如果是类型,则是类型说明语句
				return Constants.FUNC_DECLARE_SYNTAX;
			}

			if ("?".equals(second.toString())) {
				return Constants.JUDGE_INVOKE_SYNTAX;
			}

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

		// 未知
		return Constants.UNKNOWN;
	}

}
