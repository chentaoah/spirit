package com.sum.shy.api.service.lexer;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.ElementBuilder;
import com.sum.shy.api.Lexer;
import com.sum.shy.api.PostProcessor;
import com.sum.shy.api.SemanticParser;
import com.sum.shy.api.StructRecognizer;
import com.sum.shy.api.SyntaxChecker;
import com.sum.shy.api.TreeBuilder;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.element.SyntaxTree;

public class ElementBuilderImpl implements ElementBuilder {

	public SyntaxChecker checker = ProxyFactory.get(SyntaxChecker.class);

	public Lexer lexer = ProxyFactory.get(Lexer.class);

	public SemanticParser parser = ProxyFactory.get(SemanticParser.class);

	public StructRecognizer recognizer = ProxyFactory.get(StructRecognizer.class);

	public TreeBuilder builder = ProxyFactory.get(TreeBuilder.class);

	public PostProcessor processor = ProxyFactory.get(PostProcessor.class);

	@Override
	public Element build(Line line) {
		try {
			// give a chance to check the line
			checker.check(line);
			// 1.lexical analysis
			List<String> words = lexer.getWords(line.text);
			// 2.semantic analysis
			List<Token> tokens = parser.getTokens(words);
			// 3.build statement
			Statement stmt = new Statement(tokens);
			// 4.get structure grammar
			String syntax = recognizer.getSyntax(tokens);
			// 5.build an abstract syntax tree
			SyntaxTree tree = null;
			if (syntax == null) {
				tree = builder.build(stmt);
				syntax = tree.getSyntax();
			}
			// 6.generate element
			Element element = new Element(line, stmt, tree, syntax);
			// 7.post element processor
			processor.postElementProcessor(line, element);

			return element;

		} catch (Exception e) {
			System.out.println(line.debug());
			throw new RuntimeException("Exception in statement parsing!", e);
		}
	}

}
