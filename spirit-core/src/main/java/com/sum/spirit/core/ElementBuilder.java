package com.sum.spirit.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.a.lexer.Lexer;
import com.sum.spirit.core.b.build.LineChecker;
import com.sum.spirit.core.b.build.SemanticParser;
import com.sum.spirit.core.b.build.TreeBuilder;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Line;
import com.sum.spirit.pojo.element.Modifiers;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.SyntaxTree;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.SyntaxEnum;

@Component
public class ElementBuilder {

	@Autowired
	public LineChecker checker;
	@Autowired
	public Lexer lexer;
	@Autowired
	public SemanticParser parser;
	@Autowired
	public TreeBuilder builder;

	public Element build(String text) {
		return build(new Line(text));
	}

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
			// 5.语法枚举
			SyntaxEnum syntax = SyntaxTree.getSyntax(tokens);
			// 6.构建语法树
			SyntaxTree syntaxTree = null;
			if (syntax == null) {
				syntaxTree = builder.buildTree(statement);
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

	public Element rebuild(Statement statement) {
		Element element = build(statement.toString());
		// 注意：新建一个element是为了得到分析的语法，赋值是为了复用token
		element.statement = statement;
		element.tokens = statement.tokens;
		return element;
	}

}
