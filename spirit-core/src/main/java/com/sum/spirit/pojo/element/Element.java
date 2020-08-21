package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.utils.LineUtils;

public class Element extends Syntactic {

	public Line line;

	public Modifiers modifiers;

	public Statement statement;

	public AbsSyntaxTree tree;

	public String syntax;

	public List<Element> children = new ArrayList<>();

	public Element(Line line, Modifiers modifiers, Statement statement, AbsSyntaxTree tree, String syntax) {
		this.line = line;
		this.modifiers = modifiers;
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

	public boolean isModified(String keyword) {
		return modifiers.containsKeyword(keyword);
	}

	public Element addModifier(String keyword) {
		modifiers.addKeywordAtFirst(keyword);
		return this;
	}

	public Element replaceModifier(String keyword, String text) {
		modifiers.replaceKeyword(keyword, text);
		return this;
	}

	public Element insertModifier(String keyword, String text) {
		modifiers.insertKeywordAfter(keyword, text);
		return this;
	}

	public Element replaceStatement(String keyword, String text) {
		replaceKeyword(keyword, text);
		return this;
	}

	public Element removeStatement(String keyword) {
		removeKeyword(keyword);
		return this;
	}

	public Element insertStatement(String keyword, String text) {
		insertKeywordAfter(keyword, text);
		return this;
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
		return modifiers + " " + statement;
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaces(100 - line.text.length()) + ">>> " + syntax + " " + statement.debug());
		for (Element element : children)
			element.debug();
	}

}
