package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.utils.LineUtils;

public class Element extends Syntactic {

	public Line line;

	public Statement statement;

	public AbstractSyntaxTree tree;

	public String syntax;

	public List<Element> children = new ArrayList<>();

	public Element(Line line, Statement statement, AbstractSyntaxTree tree, String syntax) {
		this.line = line;
		this.statement = statement;
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
		return statement.subStmt(start, end);
	}

	public List<Statement> split(String separator) {
		return statement.split(separator);
	}

	public boolean hasChildElement() {
		return children.size() > 0;
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
		return statement.getTokens();
	}

	@Override
	public String getSyntax() {
		return syntax;
	}

	@Override
	public String toString() {
		return statement.toString();
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaces(100 - line.text.length()) + ">>> " + syntax + " " + statement.debug());
		for (Element element : children)
			element.debug();
	}

}
