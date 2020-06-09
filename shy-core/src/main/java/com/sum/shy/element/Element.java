package com.sum.shy.element;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.common.Constants;
import com.sum.shy.utils.LineUtils;

public class Element extends Syntactic {
	// 行
	public Line line;
	// 语句
	public Statement stmt;
	// 语法树
	public SyntaxTree tree;
	// 语法
	public String syntax;
	// 子节点
	public List<Element> children = new ArrayList<>();

	public Element(Line line, Statement stmt, SyntaxTree tree, String syntax) {
		this.line = line;
		this.stmt = stmt;
		this.tree = tree;
		this.syntax = syntax;
	}

	public boolean hasChild() {
		return line.hasChild();
	}

	public String getIndent() {
		return line.getIndent();
	}

	public Statement subStmt(int start, int end) {
		return stmt.subStmt(start, end);
	}

	public List<Statement> split(String separator) {
		return stmt.split(separator);
	}

	public Element replaceKeyword(String keyword, String text) {
		int index = findKeyword(keyword);
		if (index != -1)
			getTokens().set(index, new Token(Constants.KEYWORD_TOKEN, text));
		return this;
	}

	public Element removeKeyword(String keyword) {
		int index = findKeyword(keyword);
		if (index != -1)
			getTokens().remove(index);
		return this;
	}

	public Element insertAfter(String keyword, String text) {
		int index = findKeyword(keyword);
		if (index != -1)
			getTokens().add(index + 1, new Token(Constants.KEYWORD_TOKEN, text));
		return this;
	}

	@Override
	public List<Token> getTokens() {
		return stmt.getTokens();
	}

	@Override
	public String getSyntax() {
		return syntax;
	}

	@Override
	public String toString() {
		return stmt.toString();
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaces(100 - line.text.length()) + ">>> " + syntax + " " + stmt.debug());
		for (Element element : children)
			element.debug();
	}

}
