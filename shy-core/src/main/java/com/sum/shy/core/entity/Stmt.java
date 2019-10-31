package com.sum.shy.core.entity;

import java.util.List;

import com.sum.shy.core.analyzer.LexicalAnalyzer;
import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.analyzer.SyntacticParser;

public class Stmt {

	// 一行
	public String line;
	// 词法
	public List<String> words;
	// 语法
	public String syntax;
	// 语义
	public List<Token> tokens;

	public static Stmt create(String line) {
		// 1.词法分析,将语句拆分成多个单元
		List<String> words = LexicalAnalyzer.getWords(line);
		// 2.语法分析,分析语句的语法
		String syntax = SyntacticParser.getSyntax(words);
		// 3.语义分析
		List<Token> tokens = SemanticDelegate.getTokens(words);
		// 生成语句
		return new Stmt(line, words, syntax, tokens);
	}

	public Stmt(String line, List<String> words, String syntax, List<Token> tokens) {
		this.line = line;
		this.words = words;
		this.syntax = syntax;
		this.tokens = tokens;
	}

	public String get(int index) {
		return words.get(index);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			if ("keyword".equals(token.type)) {// 关键字一般后面加个空格
				sb.append(token.value.toString() + " ");

			} else if ("operator".equals(token.type)) {// 有些操作符两边加空格会好看些
				if ("&&".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else if ("=".equals(token.value)) {
					sb.append(" " + token.value + " ");
				} else {
					sb.append(token.value.toString());
				}
			} else {
				sb.append(token.value.toString());
			}
		}
		return sb.toString();
	}

}
