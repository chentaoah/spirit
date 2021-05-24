package com.sum.spirit.core.element;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.api.ElementBuilder;
import com.sum.spirit.core.api.Lexer;
import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.api.SyntaxParser;
import com.sum.spirit.core.element.action.LineChecker;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Line;
import com.sum.spirit.core.element.entity.Modifiers;
import com.sum.spirit.core.element.entity.SemanticContext;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.SyntaxResult;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class DefaultElementBuilder implements ElementBuilder {

	@Autowired
	public LineChecker checker;
	@Autowired
	public Lexer lexer;
	@Autowired
	public SemanticParser parser;
	@Autowired
	public SyntaxParser syntaxParser;

	@Override
	public Element build(String text) {
		return build(new Line(text));
	}

	@Override
	public Element build(Line line) {
		try {
			checker.check(line);
			List<String> words = lexer.getWords(line.text);
			List<Token> tokens = parser.getTokens(new SemanticContext(), words);
			Modifiers modifiers = new Modifiers(tokens);
			Statement statement = new Statement(tokens);
			SyntaxResult result = syntaxParser.parseSyntax(tokens, statement);
			Element element = new Element(line, modifiers, statement, result.syntax, result.syntaxTree);
			return element;

		} catch (Exception e) {
			line.debug();
			throw new RuntimeException("Failed to build element!", e);
		}
	}

	@Override
	public Element rebuild(Statement statement) {
		Assert.notEmpty(statement, "statement cannot be empty!");
		Element element = build(statement.toString());
		element.list = statement;// 注意：新建一个element是为了得到分析的语法，赋值是为了复用token
		return element;
	}

}
