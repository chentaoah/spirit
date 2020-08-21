package com.sum.spirit.core.lexer;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.PostProcessor;
import com.sum.spirit.api.lexer.ElementBuilder;
import com.sum.spirit.api.lexer.Lexer;
import com.sum.spirit.api.lexer.SemanticParser;
import com.sum.spirit.api.lexer.StructRecognizer;
import com.sum.spirit.api.lexer.SyntaxChecker;
import com.sum.spirit.api.lexer.TreeBuilder;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Line;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.SyntaxTree;
import com.sum.spirit.pojo.element.Token;

public class ElementBuilderImpl implements ElementBuilder {

	public static SyntaxChecker checker = ProxyFactory.get(SyntaxChecker.class);
	public static Lexer lexer = ProxyFactory.get(Lexer.class);
	public static SemanticParser parser = ProxyFactory.get(SemanticParser.class);
	public static StructRecognizer recognizer = ProxyFactory.get(StructRecognizer.class);
	public static TreeBuilder builder = ProxyFactory.get(TreeBuilder.class);
	public static PostProcessor processor = ProxyFactory.get(PostProcessor.class);

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
			Statement statement = new Statement(tokens);

			// 4.get structure grammar
			String syntax = recognizer.getSyntax(tokens);

			// 5.build an abstract syntax tree
			SyntaxTree tree = null;
			if (syntax == null) {
				tree = builder.build(statement);
				syntax = tree.getSyntax();
			}

			// 6.generate element
			Element element = new Element(line, statement, tree, syntax);

			// 7.post element processor
			processor.postElementProcessor(line, element);

			// 8.return element
			return element;

		} catch (Exception e) {
			line.debug();
			throw new RuntimeException("Failed to build element!", e);
		}
	}

}
