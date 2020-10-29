package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.pojo.enums.SyntaxEnum;
import com.sum.spirit.utils.LineUtils;

public class Element extends Syntactic {

	public Line line;

	public Modifiers modifiers;

	public Statement statement;

	public AbsSyntaxTree syntaxTree;

	public List<Element> children = new ArrayList<>();

	public Element(Line line, Modifiers modifiers, Statement statement, AbsSyntaxTree syntaxTree, SyntaxEnum syntax) {
		super(statement.tokens, syntax);
		this.line = line;
		this.modifiers = modifiers;
		this.statement = statement;
		this.syntaxTree = syntaxTree;
	}

	public String getIndent() {
		return line.getIndent();
	}

	public boolean hasChild() {
		return line.hasChild() || children.size() > 0;
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

	@Override
	public String toString() {
		return modifiers == null || modifiers.size() == 0 ? statement.toString() : modifiers + " " + statement;
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaces(100 - line.text.length()) + ">>> " + syntax + " " + statement.debug());
		for (Element element : children)
			element.debug();
	}

}
