package com.sum.spirit.pojo.element;

import java.util.List;

import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.SyntaxEnum;

public class AbsSyntaxTree {

	public static final int START_INDEX = 0;

	public List<Node> nodes;

	public AbsSyntaxTree(List<Node> nodes) {
		this.nodes = nodes;
	}

	public SyntaxEnum getSyntax() {

		try {
			Node firstNode = nodes.get(START_INDEX);
			Token firstToken = firstNode.token;

			// 如果是行级别关键字，则直接返回语法枚举
			if (KeywordEnum.isLine(firstToken.toString()))
				return SyntaxEnum.valueOf(firstToken.toString().toUpperCase());

			// 如果只有一个节点，可能是方法调用
			if (nodes.size() == 1 && firstToken.isLocalMethod()) {
				String memberName = firstToken.getAttribute(AttributeEnum.MEMBER_NAME);
				if (KeywordEnum.SUPER.value.equals(memberName)) {
					return SyntaxEnum.SUPER;

				} else if (KeywordEnum.THIS.value.equals(memberName)) {
					return SyntaxEnum.THIS;
				}
				return SyntaxEnum.INVOKE;
			}

			// 通过抽象语法树进行推导
			if (nodes.size() == 1) {
				if (firstToken.isType()) {
					Token nextToken = firstNode.next.token;
					if (nextToken.isVar()) { // String text
						return SyntaxEnum.DECLARE;

					} else if (nextToken.isLocalMethod()) { // String test()
						return SyntaxEnum.FUNC_DECLARE;
					}
				} else if (firstToken.isAssign()) {
					Token prevToken = firstNode.prev.token;
					if (prevToken.isType()) {// String text = "abc"
						return SyntaxEnum.DECLARE_ASSIGN;

					} else if (prevToken.isVar()) {// text = "abc"
						return SyntaxEnum.ASSIGN;

					} else if (prevToken.isVisitField()) {// var.text = "abc"
						return SyntaxEnum.FIELD_ASSIGN;
					}
				} else if (firstToken.isInvokeMethod()) {// list.get(0)
					return SyntaxEnum.INVOKE;
				}
				return SyntaxEnum.INVOKE;
			}

			if (nodes.size() == 2) {
				if (firstToken.isType()) {
					Token nextToken = firstNode.next.token;
					if (nextToken.isLocalMethod()) // String test() {
						return SyntaxEnum.FUNC_DECLARE;
				}
			}

			Token secondToken = nodes.get(START_INDEX + 1).token;
			Token thirdToken = nodes.get(START_INDEX + 2).token;

			if (KeywordEnum.FOR.value.equals(firstToken.toString())) {
				// for ? in ? {
				if (KeywordEnum.IN.value.equals(thirdToken.toString()))
					return SyntaxEnum.FOR_IN;
				return SyntaxEnum.FOR;// for ?; ?; ? {
			}

			if ("}".equals(firstToken.toString())) {
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

		} catch (Exception e) {
			throw new RuntimeException("Unable to get current syntax!tokens of tree:" + nodes.toString());
		}

		throw new RuntimeException("Unknown syntax!");

	}

}
