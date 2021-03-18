package com.sum.spirit.core.element.entity;

import java.util.List;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.SyntaxEnum;

public class SyntaxTree {

	public List<Node> nodes;

	public SyntaxTree(List<Node> nodes) {
		this.nodes = nodes;
	}

	public static SyntaxEnum getSimpleSyntax(List<Token> tokens) {
		Token first = tokens.get(0);
		// keyword
		if (KeywordEnum.isStruct(first.toString())) {
			return SyntaxEnum.valueOf(first.toString().toUpperCase());
		}
		// end
		if (tokens.size() == 1 && "}".equals(first.toString())) {
			return SyntaxEnum.END;
		}
		// annotation
		if (tokens.size() == 1 && first.isAnnotation()) {
			return SyntaxEnum.ANNOTATION;
		}
		// unknown
		return null;
	}

	public SyntaxEnum getSyntax() {

		SyntaxEnum syntax = getLineSyntax();
		if (syntax != null) {
			return syntax;
		}

		syntax = getSyntaxByOneNode();
		if (syntax != null) {
			return syntax;
		}

		syntax = getSyntaxByTwoNodes();
		if (syntax != null) {
			return syntax;
		}

		syntax = getSyntaxByThreeNodes();
		if (syntax != null) {
			return syntax;
		}

		throw new RuntimeException("Unknown syntax!");
	}

	public SyntaxEnum getLineSyntax() {

		Node firstNode = nodes.get(0);
		Token firstToken = firstNode.token;

		// 如果是行级别关键字，则直接返回语法枚举
		if (!firstNode.canSplit() && KeywordEnum.isLine(firstToken.toString())) {
			return SyntaxEnum.valueOf(firstToken.toString().toUpperCase());
		}

		return null;
	}

	public SyntaxEnum getSyntaxByOneNode() {

		if (nodes.size() != 1) {
			return null;
		}

		Node firstNode = nodes.get(0);
		Token firstToken = firstNode.token;

		// 如果只有一个节点，可能是方法调用
		if (firstToken.isLocalMethod()) {
			String memberName = firstToken.attr(AttributeEnum.MEMBER_NAME);
			if (KeywordEnum.SUPER.value.equals(memberName)) {
				return SyntaxEnum.SUPER;
			} else if (KeywordEnum.THIS.value.equals(memberName)) {
				return SyntaxEnum.THIS;
			}
			return SyntaxEnum.INVOKE;
		}

		// 通过抽象语法树进行推导
		if (firstToken.isType()) {
			Token nextToken = firstNode.next.token;
			if (nextToken.isVariable()) { // String text
				return SyntaxEnum.DECLARE;
			} else if (nextToken.isLocalMethod()) { // String test()
				return SyntaxEnum.FUNC_DECLARE;
			}
		} else if (firstToken.isAssign()) {
			Token prevToken = firstNode.prev.token;
			if (prevToken.isType()) {// String text = "abc"
				return SyntaxEnum.DECLARE_ASSIGN;
			} else if (prevToken.isVariable()) {// text = "abc"
				return SyntaxEnum.ASSIGN;
			} else if (prevToken.isVisitField()) {// var.text = "abc"
				return SyntaxEnum.FIELD_ASSIGN;
			}
		} else if (firstToken.isVisitMethod()) {// list.get(0)
			return SyntaxEnum.INVOKE;
		}

		throw new RuntimeException("Unknown syntax!");
	}

	public SyntaxEnum getSyntaxByTwoNodes() {

		if (nodes.size() != 2) {
			return null;
		}

		Node firstNode = nodes.get(0);
		Token firstToken = firstNode.token;
		if (firstToken.isType()) {
			Token nextToken = firstNode.next.token;
			if (nextToken.isLocalMethod()) {
				return SyntaxEnum.FUNC_DECLARE;
			}
		}

		throw new RuntimeException("Unknown syntax!");
	}

	public SyntaxEnum getSyntaxByThreeNodes() {

		Token firstToken = nodes.get(0).token;
		Token secondToken = nodes.get(1).token;
		Token thirdToken = nodes.get(2).token;

		if (KeywordEnum.FOR.value.equals(firstToken.toString())) {
			if (secondToken.isSubexpress()) {
				return SyntaxEnum.FOR;// for (i=0; i<10; i++) {
			}
			if (KeywordEnum.IN.value.equals(thirdToken.toString())) {
				return SyntaxEnum.FOR_IN;// for ? in ? {
			}
			throw new RuntimeException("Unknown syntax!");
		}

		if (SymbolEnum.RIGHT_CURLY_BRACKET.value.equals(firstToken.toString())) {
			if (KeywordEnum.ELSE.value.equals(secondToken.toString())) {
				if (KeywordEnum.IF.value.equals(thirdToken.toString())) {// } else if ? {
					return SyntaxEnum.ELSE_IF;
				} else {
					return SyntaxEnum.ELSE;// } else {
				}
			} else if (KeywordEnum.CATCH.value.equals(secondToken.toString())) {// } catch Exception x {
				return SyntaxEnum.CATCH;
			} else if (KeywordEnum.FINALLY.value.equals(secondToken.toString())) {// } finally {
				return SyntaxEnum.FINALLY;
			}
		}

		throw new RuntimeException("Unknown syntax!");
	}

	@Override
	public String toString() {
		throw new RuntimeException("The method toString() should not be called");
	}

}
