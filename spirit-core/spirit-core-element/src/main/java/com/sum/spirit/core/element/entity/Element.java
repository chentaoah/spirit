package com.sum.spirit.core.element.entity;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.common.enums.SyntaxEnum;
import com.sum.spirit.common.utils.LineUtils;
import com.sum.spirit.core.element.frame.Syntactic;

public class Element extends Syntactic {

	public Line line;
	public Modifiers modifiers;
	public Statement statement;
	public SyntaxTree syntaxTree;
	public List<Element> children = new ArrayList<>();

	public Element(Line line, Modifiers modifiers, Statement statement, SyntaxTree syntaxTree, SyntaxEnum syntax) {
		super(syntax, statement.tokens);
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

	public Element replaceModifier(String keyword, String newKeyword) {
		modifiers.replaceKeyword(keyword, newKeyword);
		return this;
	}

	public Element insertModifier(String keyword, String newKeyword) {
		modifiers.insertKeywordAfter(keyword, newKeyword);
		return this;
	}

	public Statement subStmt(int start, int end) {
		return statement.subStmt(start, end);
	}

	public List<Statement> splitStmt(String separator) {
		return statement.splitStmt(separator);
	}

	@Override
	public String toString() {
		return modifiers == null || modifiers.size() == 0 ? statement.toString() : modifiers + " " + statement;
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaces(100 - line.text.length()) + ">>> " + syntax + " " + statement.debug());
		for (Element element : children) {
			element.debug();
		}
	}

}
