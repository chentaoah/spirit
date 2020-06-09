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
import com.sum.shy.element.Tree;

public class ElementBuilderImpl implements ElementBuilder {

	public Lexer lexer = ProxyFactory.get(Lexer.class);

	public SemanticParser parser = ProxyFactory.get(SemanticParser.class);

	public StructRecognizer recognizer = ProxyFactory.get(StructRecognizer.class);

	public TreeBuilder builder = ProxyFactory.get(TreeBuilder.class);

	@Override
	public Element build(Line line) {
		try {
			// 1.lexical analysis
			List<String> words = lexer.getWords(line.text);
			// 2.semantic analysis
			List<Token> tokens = parser.getTokens(words);
			// 3.build statement
			Stmt stmt = new Stmt(tokens);
			// 4.get structure grammar
			String syntax = recognizer.getSyntax(tokens);
			// 5.build an abstract syntax tree
			Tree tree = null;
			if (syntax == null) {
				tree = builder.build(stmt);
				syntax = tree.getSyntax();
			}
			// 6.generate element
			return new Element(line, stmt, tree, syntax);

		} catch (Exception e) {
			System.out.println(line.debug());
			throw new RuntimeException("Exception in statement parsing!", e);
		}
	}

}
