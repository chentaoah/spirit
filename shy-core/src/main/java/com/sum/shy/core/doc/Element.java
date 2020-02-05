package com.sum.shy.core.doc;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.lexical.LexicalAnalyzer;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.lexical.StructRecognizer;
import com.sum.shy.core.lexical.TreeBuilder;
import com.sum.shy.utils.LineUtils;

@SuppressWarnings("serial")
public class Element extends ArrayList<Element> {
	// 行
	public Line line;
	// 语句
	public Stmt stmt;
	// 语法树
	public Tree tree;
	// 语法
	public String syntax;

	public Element(Line line) {
		this.line = line;
		init(line);
	}

	private void init(Line line) {
		// 1.单词拆分
		List<String> words = LexicalAnalyzer.getWords(line.text);
		// 2.按照基本类型,获取tokens
		this.stmt = new Stmt(SemanticDelegate.getTokens(words));

		String syntax = StructRecognizer.getStructSyntax(words);
		if (syntax != null) {
			// 3.设置语法
			this.syntax = syntax;
		} else {
			// 3.建立抽象语法树
			this.tree = TreeBuilder.build(stmt);
			// 4.设置语法
			this.syntax = tree.getSyntax();
		}

	}

	public boolean hasChild() {
		return line.text.trim().endsWith("{");
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaceByNumber(100 - line.text.length()) + ">>> " + syntax + " "
				+ stmt.debug());
		for (Element element : this)
			element.debug();
	}

}
