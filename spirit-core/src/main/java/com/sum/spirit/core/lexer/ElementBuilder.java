package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.LineChecker;
import com.sum.spirit.core.PostProcessor;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Line;
import com.sum.spirit.pojo.element.Modifiers;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.AbsSyntaxTree;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.SyntaxEnum;

@Component
public class ElementBuilder {

	@Autowired(required = false)
	public LineChecker checker;
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

	public Element build(String text) {
		return build(new Line(text));
	}

	public Element build(Line line) {
		try {
			// 语法校验
			if (checker != null)
				checker.check(line);
			// 1.词法分析
			List<String> words = lexer.getWords(line.text);
			// 2.语义分析
			List<Token> tokens = parser.getTokens(words);
			// 3.修饰词
			Modifiers modifiers = new Modifiers(tokens);
			// 4.语句
			Statement statement = new Statement(tokens);
			// 5.语法枚举
			SyntaxEnum syntax = recognizer.getSyntax(tokens);
			// 6.构建语法树
			AbsSyntaxTree syntaxTree = null;
			if (syntax == null) {
				syntaxTree = builder.build(statement);
				syntax = syntaxTree.getSyntax();
			}
			// 7.创建元素
			Element element = new Element(line, modifiers, statement, syntaxTree, syntax);
			// 8.返回元素
			return element;

		} catch (Exception e) {
			line.debug();
			throw new RuntimeException("Failed to build element!", e);
		}
	}

}
