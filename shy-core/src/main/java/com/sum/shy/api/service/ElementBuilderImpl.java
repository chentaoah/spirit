package com.sum.shy.api.service;

import java.util.List;

import com.sum.shy.api.ElementBuilder;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;
import com.sum.shy.lexical.LexicalAnalyzer;
import com.sum.shy.lexical.SemanticDelegate;
import com.sum.shy.lexical.StructRecognizer;
import com.sum.shy.lexical.TreeBuilder;

public class ElementBuilderImpl implements ElementBuilder {

	@Override
	public Element buildElement(Line line) {
		try {
			Element element = new Element();
			// 1.词法拆分
			List<String> words = LexicalAnalyzer.getWords(line.text);
			// 2.token流
			List<Token> tokens = SemanticDelegate.getTokens(words);
			// 3.生成语句
			element.stmt = new Stmt(tokens);
			// 4.一些基本的结构语法，不需要复杂分析的
			element.syntax = StructRecognizer.getSyntax(tokens);
			// 如果不是结构语法，则使用抽象语法树推导
			if (element.syntax == null) {
				// 5.建立抽象语法树
				element.tree = TreeBuilder.build(element.stmt);
				// 6.获取语法
				element.syntax = element.tree.getSyntax();
			}
			return element;

		} catch (Exception e) {
			System.out.println(line.debug());
			throw new RuntimeException("Exception in statement parsing!", e);
		}
	}

}
