package com.gitee.spirit.core.element;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.api.Lexer;
import com.gitee.spirit.core.api.SemanticParser;
import com.gitee.spirit.core.api.SyntaxParser;
import com.gitee.spirit.core.element.action.LineChecker;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Line;
import com.gitee.spirit.core.element.entity.Modifiers;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.SyntaxResult;
import com.gitee.spirit.core.element.entity.Token;

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
			List<Token> tokens = parser.getTokens(words);
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
	public Element build(Statement statement) {
		Assert.notEmpty(statement, "Statement cannot be empty!");
		SyntaxResult result = syntaxParser.parseSyntax(statement.list, statement);
		Element element = new Element(null, null, statement, result.syntax, result.syntaxTree);
		return element;
	}

}
