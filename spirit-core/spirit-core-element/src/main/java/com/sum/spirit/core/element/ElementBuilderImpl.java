package com.sum.spirit.core.element;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.SyntaxEnum;
import com.sum.spirit.core.api.ElementBuilder;
import com.sum.spirit.core.api.Lexer;
import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.element.action.LineChecker;
import com.sum.spirit.core.element.action.SyntaxRecognizer;
import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Line;
import com.sum.spirit.core.element.entity.Modifiers;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class ElementBuilderImpl implements ElementBuilder {

	@Autowired
	public LineChecker checker;
	@Autowired
	public Lexer lexer;
	@Autowired
	public SemanticParser parser;
	@Autowired
	public SyntaxRecognizer recognizer;

	@Override
	public Element build(String text) {
		return build(new Line(text));
	}

	@Override
	public Element build(Line line) {
		try {
			// 语法校验
			checker.check(line);
			// 1.词法分析
			List<String> words = lexer.getWords(line.text);
			// 2.语义分析
			List<Token> tokens = parser.getTokens(words);
			// 3.修饰词
			Modifiers modifiers = new Modifiers(tokens);
			// 4.语句
			Statement statement = new Statement(tokens);
			// 5.分析语法
			Object[] result = recognizer.parseSyntax(tokens, statement);
			SyntaxEnum syntax = (SyntaxEnum) result[0];
			SyntaxTree syntaxTree = (SyntaxTree) result[1];
			// 6.创建元素
			Element element = new Element(line, modifiers, statement, syntaxTree, syntax);
			// 7.返回元素
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
