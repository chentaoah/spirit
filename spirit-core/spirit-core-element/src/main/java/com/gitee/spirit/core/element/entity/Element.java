package com.gitee.spirit.core.element.entity;

import java.util.ArrayList;
import java.util.List;

import com.gitee.spirit.common.enums.SyntaxEnum;
import com.gitee.spirit.common.utils.LineUtils;
import com.gitee.spirit.core.element.frame.Syntactic;

public class Element extends Syntactic {

	public Line line;
	public Modifiers modifiers;
	public SyntaxTree syntaxTree;
	public List<Element> children = new ArrayList<>();

	public Element(Line line, Modifiers modifiers, Statement statement, SyntaxEnum syntax, SyntaxTree syntaxTree) {
		super(syntax, statement);
		this.line = line;
		this.modifiers = modifiers;
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

	public Element addModifiers(String... keywords) {
		for (int i = keywords.length - 1; i >= 0; i--) {
			modifiers.addKeywordAtFirst(keywords[i]);
		}
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

	@Override
	public String toString() {
		return modifiers == null || modifiers.size() == 0 ? super.toString() : modifiers + " " + super.toString();
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaces(100 - line.text.length()) + ">>> " + syntax + " " + super.debugTokens());
		for (Element element : children) {
			element.debug();
		}
	}

}
