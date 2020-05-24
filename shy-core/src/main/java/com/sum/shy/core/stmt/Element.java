package com.sum.shy.core.stmt;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.lexical.LexicalAnalyzer;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.lexical.StructRecognizer;
import com.sum.shy.core.lexical.TreeBuilder;
import com.sum.shy.core.stmt.api.Syntactic;
import com.sum.shy.core.utils.LineUtils;

public class Element extends Syntactic {
	// 行
	public Line line;
	// 语句
	public Stmt stmt;
	// 语法树
	public Tree tree;
	// 语法
	public String syntax;
	// 子节点
	public List<Element> children = new ArrayList<>();

	public Element(String text) {
		this(new Line(text));
	}

	public Element(Line line) {
		this.line = line;
		init(line);
	}

	public void init(Line line) {
		try {
			// 1.词法拆分
			List<String> words = LexicalAnalyzer.getWords(line.text);
			// 2.token流
			List<Token> tokens = SemanticDelegate.getTokens(words);
			// 3.生成语句
			this.stmt = new Stmt(tokens);
			// 4.一些基本的结构语法，不需要复杂分析的
			this.syntax = StructRecognizer.getSyntax(tokens);
			// 如果不是结构语法，则使用抽象语法树推导
			if (syntax == null) {
				// 5.建立抽象语法树
				this.tree = TreeBuilder.build(stmt);
				// 6.获取语法
				this.syntax = tree.getSyntax();
			}

		} catch (Exception e) {
			System.out.println(line.debug());
			throw new RuntimeException("Exception in statement parsing!", e);
		}
	}

	public boolean hasChild() {
		return line.hasChild();
	}

	public String getIndent() {
		return line.getIndent();
	}

	public Stmt subStmt(int start, int end) {
		return stmt.subStmt(start, end);
	}

	public List<Stmt> split(String separator) {
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
		System.out.println(
				line.text + LineUtils.getSpaces(100 - line.text.length()) + ">>> " + syntax + " " + stmt.debug());
		for (Element element : children)
			element.debug();
	}

}
