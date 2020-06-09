package com.sum.shy.api.service.lexer;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.ElementBuilder;
import com.sum.shy.api.Lexer;
import com.sum.shy.api.SemanticParser;
import com.sum.shy.api.StructRecognizer;
import com.sum.shy.api.TreeBuilder;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;

public class ElementBuilderImpl implements ElementBuilder {

	public Lexer lexer = ProxyFactory.get(Lexer.class);

	public SemanticParser parser = ProxyFactory.get(SemanticParser.class);

	public StructRecognizer recognizer = ProxyFactory.get(StructRecognizer.class);

	public TreeBuilder builder = ProxyFactory.get(TreeBuilder.class);

	@Override
	public Element buildElement(Line line) {
		try {
			Element element = new Element();

			element.line = line;
			// 1.词法拆分
			List<String> words = lexer.getWords(line.text);
			// 2.token流
			List<Token> tokens = parser.getTokens(words);
			// 3.生成语句
			element.stmt = new Stmt(tokens);
			// 4.一些基本的结构语法，不需要复杂分析的
			element.syntax = recognizer.getSyntax(tokens);
			// 如果不是结构语法，则使用抽象语法树推导
			if (element.syntax == null) {
				// 5.建立抽象语法树
				element.tree = builder.build(element.stmt);
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
