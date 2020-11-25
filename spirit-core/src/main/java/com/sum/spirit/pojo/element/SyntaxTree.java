package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.SymbolEnum;
import com.sum.spirit.pojo.enums.SyntaxEnum;
import com.sum.spirit.utils.LineUtils;

public class SyntaxTree {

	public static final int START_INDEX = 0;

	public List<Node> nodes;

	public SyntaxTree(List<Node> nodes) {
		this.nodes = nodes;
	}

	public static SyntaxEnum getSyntax(List<Token> tokens) {
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
		if (syntax != null)
			return syntax;

		syntax = getSyntaxByOneNode();
		if (syntax != null)
			return syntax;

		syntax = getSyntaxByTwoNodes();
		if (syntax != null)
			return syntax;

		syntax = getSyntaxByThreeNodes();
		if (syntax != null)
			return syntax;

		throw new RuntimeException("Unknown syntax!");
	}

	public SyntaxEnum getLineSyntax() {

		Node firstNode = nodes.get(START_INDEX);
		Token firstToken = firstNode.token;

		// 如果是行级别关键字，则直接返回语法枚举
		if (!firstNode.canSplit() && KeywordEnum.isLine(firstToken.toString()))
			return SyntaxEnum.valueOf(firstToken.toString().toUpperCase());

		return null;
	}

	public SyntaxEnum getSyntaxByOneNode() {

		if (nodes.size() != 1)
			return null;

		Node firstNode = nodes.get(START_INDEX);
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

		throw new RuntimeException("Unknown syntax!");
	}

	public SyntaxEnum getSyntaxByTwoNodes() {

		if (nodes.size() != 2)
			return null;

		Node firstNode = nodes.get(START_INDEX);
		Token firstToken = firstNode.token;
		if (firstToken.isType()) {
			Token nextToken = firstNode.next.token;
			if (nextToken.isLocalMethod()) // String test() {
				return SyntaxEnum.FUNC_DECLARE;
		}

		throw new RuntimeException("Unknown syntax!");
	}

	public SyntaxEnum getSyntaxByThreeNodes() {

		Token firstToken = nodes.get(START_INDEX).token;
		Token secondToken = nodes.get(START_INDEX + 1).token;
		Token thirdToken = nodes.get(START_INDEX + 2).token;

		if (KeywordEnum.FOR.value.equals(firstToken.toString())) {
			if (secondToken.isSubexpress())
				return SyntaxEnum.FOR;// for (i=0; i<10; i++) {
			if (KeywordEnum.IN.value.equals(thirdToken.toString()))
				return SyntaxEnum.FOR_IN;// for ? in ? {
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

	public String debug() {
		// 生成打印的面板
		List<Line> lines = new ArrayList<>();
		for (int i = 0; i < 20; i++)
			lines.add(new Line(i + 1, LineUtils.getSpaces(150)));

		// 遍历节点，并构造树结构
		buildTree(lines, 0, nodes);

		// 打印
		StringBuilder builder = new StringBuilder();
		for (Line line : lines) {
			if (!line.isIgnore())
				builder.append(line.text + "\n");
		}
		return builder.toString();
	}

	public void buildTree(List<Line> lines, int depth, List<Node> nodes) {
		for (Node node : nodes)
			buildTree(lines, depth, "", node);
	}

	public void buildTree(List<Line> lines, int depth, String separator, Node node) {

		Token token = node.token;
		int position = token.attr(AttributeEnum.POSITION);
		int length = token.attr(AttributeEnum.LENGTH);

		// 尽量上上面的分割符在中间,奇数在中间,偶数在中间偏左一个
		if (StringUtils.isNotEmpty(separator))
			println(lines, depth - 1, position + length / 2 + length % 2 - 1, separator);

		// 如果该节点是语法树
		if (node.canSplit()) {
			SyntaxTree syntaxTree = token.getValue();
			buildTree(lines, depth, syntaxTree.nodes);

		} else {
			println(lines, depth, position, token.toString());
		}

		// 左边节点
		if (node.prev != null)
			buildTree(lines, depth + 2, "/", node.prev);

		// 右边节点
		if (node.next != null)
			buildTree(lines, depth + 2, "\\", node.next);
	}

	public void println(List<Line> lines, int depth, int position, String text) {
		Line line = lines.get(depth);
		StringBuilder builder = new StringBuilder(line.text);
		builder.replace(position, position + text.length(), text);
		line.text = builder.toString();
	}

}
