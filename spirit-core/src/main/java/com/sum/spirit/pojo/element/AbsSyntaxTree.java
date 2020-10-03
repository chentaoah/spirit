package com.sum.spirit.pojo.element;

import java.util.List;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.KeywordTable;
import com.sum.spirit.pojo.common.SyntaxEnum;

public class AbsSyntaxTree {

	public List<Token> tokens;

	public AbsSyntaxTree(List<Token> tokens) {
		this.tokens = tokens;
	}

	public SyntaxEnum getSyntax() {

		try {
			int start = 0;
			Token first = tokens.get(start);

			if (KeywordTable.isLine(first.toString()))
				return SyntaxEnum.valueOf(first.toString().toUpperCase());

			// if there is only one element, it may be a local method call
			if (tokens.size() == 1 && first.isLocalMethod()) {
				if (Constants.SUPER_KEYWORD.equals(first.getMemberName())) {
					return SyntaxEnum.SUPER;

				} else if (Constants.THIS_KEYWORD.equals(first.getMemberName())) {
					return SyntaxEnum.THIS;
				}
				return SyntaxEnum.INVOKE;
			}

			// deriving syntax from abstract syntax tree
			if (tokens.size() == 1 && first.isNode()) {
				Node node = first.getValue();
				Token token = node.token;
				if (token.isType()) {
					Token rightToken = node.right.token;
					if (rightToken.isVar()) { // String text
						return SyntaxEnum.DECLARE;

					} else if (rightToken.isLocalMethod()) { // String test()
						return SyntaxEnum.FUNC_DECLARE;
					}
				} else if (token.isAssign()) {
					Token leftToken = node.left.token;
					if (leftToken.isType()) {// String text = "abc"
						return SyntaxEnum.DECLARE_ASSIGN;

					} else if (leftToken.isVar()) {// text = "abc"
						return SyntaxEnum.ASSIGN;

					} else if (leftToken.isVisitField()) {// var.text = "abc"
						return SyntaxEnum.FIELD_ASSIGN;
					}
				} else if (token.isInvokeMethod()) {// list.get(0)
					return SyntaxEnum.INVOKE;
				}
				return SyntaxEnum.INVOKE;
			}

			if (tokens.size() == 2 && first.isNode()) {
				Node node = first.getValue();
				Token token = node.token;
				if (token.isType()) {
					Token rightToken = node.right.token;
					if (rightToken.isLocalMethod()) // String test() {
						return SyntaxEnum.FUNC_DECLARE;
				}
			}

			Token second = tokens.get(start + 1);
			Token third = tokens.get(start + 2);

			if (Constants.FOR_KEYWORD.equals(first.toString())) {
				if (Constants.IN_KEYWORD.equals(third.toString())) {// for ? in ? {
					return SyntaxEnum.FOR_IN;
				}
				return SyntaxEnum.FOR;// for ?; ?; ? {
			}

			if ("}".equals(first.toString())) {
				if (Constants.ELSE_KEYWORD.equals(second.toString())) {
					if (Constants.IF_KEYWORD.equals(third.toString())) {// } else if ? {
						return SyntaxEnum.ELSEIF;
					} else {
						return SyntaxEnum.ELSE;// } else {
					}
				} else if (Constants.CATCH_KEYWORD.equals(second.toString())) {// } catch Exception x {
					return SyntaxEnum.CATCH;

				} else if (Constants.FINALLY_KEYWORD.equals(second.toString())) {// } finally {
					return SyntaxEnum.FINALLY;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Unable to get current syntax!tokens of tree:" + tokens.toString());
		}

		throw new RuntimeException("Unknown syntax!");

	}

}
