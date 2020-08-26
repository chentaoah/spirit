package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.PostProcessor;
import com.sum.spirit.api.lexer.ElementBuilder;
import com.sum.spirit.api.lexer.Lexer;
import com.sum.spirit.api.lexer.SemanticParser;
import com.sum.spirit.api.lexer.StructRecognizer;
import com.sum.spirit.api.lexer.SyntaxChecker;
import com.sum.spirit.api.lexer.TreeBuilder;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Line;
import com.sum.spirit.pojo.element.Modifiers;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.AbsSyntaxTree;
import com.sum.spirit.pojo.element.Token;

@Component
public class ElementBuilderImpl implements ElementBuilder {

	@Autowired(required = false)
	public SyntaxChecker checker;

	@Autowired
	public Lexer lexer;

	@Autowired
	public SemanticParser parser;

	@Autowired
	public StructRecognizer recognizer;

	@Autowired
	public TreeBuilder builder;

	@Autowired
	public PostProcessor processor;

	@Override
	public Element build(Line line) {
		try {
			// give a chance to check the line
			if (checker != null)
				checker.check(line);

			// 1.lexical analysis
			List<String> words = lexer.getWords(line.text);

			// 2.semantic analysis
			List<Token> tokens = parser.getTokens(words);

			// 3.get modifiers
			Modifiers modifiers = new Modifiers(tokens);

			// 4.build statement
			Statement statement = new Statement(tokens);

			// 5.get structure grammar
			String syntax = recognizer.getSyntax(tokens);

			// 6.build an abstract syntax tree
			AbsSyntaxTree syntaxTree = null;
			if (StringUtils.isEmpty(syntax)) {
				syntaxTree = builder.build(statement);
				syntax = syntaxTree.getSyntax();
			}

			// 7.generate element
			Element element = new Element(line, modifiers, statement, syntaxTree, syntax);

			// 8.post element processor
			processor.postElementProcessor(line, element);

			// 9.return element
			return element;

		} catch (Exception e) {
			line.debug();
			throw new RuntimeException("Failed to build element!", e);
		}
	}

}
