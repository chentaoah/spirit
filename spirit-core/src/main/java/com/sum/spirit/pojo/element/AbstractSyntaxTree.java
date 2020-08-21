package com.sum.spirit.pojo.element;

import java.util.List;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.KeywordTable;

public class AbstractSyntaxTree {

	public List<Token> tokens;

	public AbstractSyntaxTree(List<Token> tokens) {
		this.tokens = tokens;
	}

	public String getSyntax() {

		try {
			int start = 0;
			Token first = tokens.get(start);

			if (KeywordTable.isLine(first.toString()))
				return first.toString();

			// if there is only one element, it may be a local method call
			if (tokens.size() == 1 && first.isLocalMethod()) {
				if (Constants.SUPER_KEYWORD.equals(first.getMemberName())) {
					return Constants.SUPER_SYNTAX;

				} else if (Constants.THIS_KEYWORD.equals(first.getMemberName())) {
					return Constants.THIS_SYNTAX;
				}
				return Constants.INVOKE_SYNTAX;
			}

			// deriving syntax from abstract syntax tree
			if (tokens.size() == 1 && first.isNode()) {
				Node node = first.getValue();
				Token token = node.token;
				if (token.isType()) {
					Token rightToken = node.right.token;
					if (rightToken.isVar()) { // String text
						return Constants.DECLARE_SYNTAX;

					} else if (rightToken.isLocalMethod()) { // String test()
						return Constants.FUNC_DECLARE_SYNTAX;
					}
				} else if (token.isAssign()) {
					Token leftToken = node.left.token;
					if (leftToken.isType()) {// String text = "abc"
						return Constants.DECLARE_ASSIGN_SYNTAX;

					} else if (leftToken.isVar()) {// text = "abc"
						return Constants.ASSIGN_SYNTAX;

					} else if (leftToken.isVisitField()) {// var.text = "abc"
						return Constants.FIELD_ASSIGN_SYNTAX;
					}
				} else if (token.isInvokeMethod()) {// list.get(0)
					return Constants.INVOKE_SYNTAX;
				}
				return Constants.INVOKE_SYNTAX;
			}

			if (tokens.size() == 2 && first.isNode()) {
				Node node = first.getValue();
				Token token = node.token;
				if (token.isType()) {
					Token rightToken = node.right.token;
					if (rightToken.isLocalMethod()) // String test() {
						return Constants.FUNC_DECLARE_SYNTAX;
				}
			}

			Token second = tokens.get(start + 1);
			Token third = tokens.get(start + 2);

			if (Constants.FOR_KEYWORD.equals(first.toString())) {
				if (Constants.IN_KEYWORD.equals(third.toString())) {// for ? in ? {
					return Constants.FOR_IN_SYNTAX;
				}
				return Constants.FOR_SYNTAX;// for ?; ?; ? {
			}

			if ("}".equals(first.toString())) {
				if (Constants.ELSE_KEYWORD.equals(second.toString())) {
					if (Constants.IF_KEYWORD.equals(third.toString())) {// } else if ? {
						return Constants.ELSEIF_SYNTAX;
					} else {
						return Constants.ELSE_SYNTAX;// } else {
					}
				} else if (Constants.CATCH_KEYWORD.equals(second.toString())) {// } catch Exception x {
					return Constants.CATCH_SYNTAX;

				} else if (Constants.FINALLY_KEYWORD.equals(second.toString())) {// } finally {
					return Constants.FINALLY_SYNTAX;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Unable to get current syntax!tokens of tree:" + tokens.toString());
		}

		throw new RuntimeException("Unknown syntax!");

	}

}
