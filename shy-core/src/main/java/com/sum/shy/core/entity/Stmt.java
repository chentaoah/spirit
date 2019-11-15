package com.sum.shy.core.entity;

import java.util.List;

import com.sum.shy.core.analyzer.LexicalAnalyzer;
import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.analyzer.SyntacticParser;

public class Stmt {

	// 一行
	public Line line;
	// 词法
	public List<String> words;
	// 语法
	public String syntax;
	// 语义
	public List<Token> tokens;

	public static Stmt create(Line line) {
		// 1.词法分析,将语句拆分成多个单元
		List<String> words = LexicalAnalyzer.getWords(line.text);
		// 2.语法分析,分析语句的语法
		String syntax = SyntacticParser.getSyntax(words);
		// 3.语义分析
		List<Token> tokens = SemanticDelegate.getTokens(syntax, words);
		// 生成语句
		return new Stmt(line, words, syntax, tokens);
	}

	public static Stmt create(String text) {
		return create(new Line(null, text));
	}

	public Stmt(Line line, List<String> words, String syntax, List<Token> tokens) {
		this.line = line;
		this.words = words;
		this.syntax = syntax;
		this.tokens = tokens;
	}

	public Stmt(String word, List<String> subWords, List<Token> subTokens) {
		this.line = new Line(null, word);
		this.words = subWords;
		this.tokens = subTokens;
	}

	public String get(int index) {
		return words.get(index);
	}

	public Token getToken(int index) {
		return tokens.get(index);
	}

	public int size() {
		return tokens.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			if (token.isKeyword()) {// 关键字一般后面加个空格
				sb.append(token.value.toString() + " ");
			} else if (token.isOperator()) {// 有些操作符两边加空格会好看些
				if ("&&".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else if ("||".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else if ("=".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else if ("==".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else if ("!=".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else if (">".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else if ("<".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else {
					sb.append(token.value.toString());
				}
			} else if (token.isType()) {// 类型声明后面加空格
				// 根语句类型后面加个空格,子语句不用加
				if (syntax != null) {
					sb.append(token.value.toString() + " ");
				} else {
					sb.append(token.value.toString());
				}
			} else {
				sb.append(token.value.toString());
			}
		}
		return sb.toString();
	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			sb.append(token.toString() + " ");
		}
		return sb.toString();
	}

	public boolean isAssignment() {
		return Constants.ASSIGNMENT_SYNTAX.equals(syntax);
	}

	public boolean isIf() {
		return Constants.IF_SYNTAX.equals(syntax);
	}

	public boolean isElseIf() {
		return Constants.ELSEIF_SYNTAX.equals(syntax);
	}

	public boolean isElse() {
		return Constants.ELSE_SYNTAX.equals(syntax);
	}

	public boolean isEnd() {
		return Constants.END_SYNTAX.equals(syntax);
	}

	public boolean isReturn() {
		return Constants.RETURN_SYNTAX.equals(syntax);
	}

}
