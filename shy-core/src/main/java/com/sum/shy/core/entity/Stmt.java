package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.LexicalAnalyzer;
import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.analyzer.SyntacticDefiner;

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
		String syntax = SyntacticDefiner.getSyntax(words);
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

	public Stmt(String text) {
		this.line = new Line(null, text);
		this.words = new ArrayList<>();
		this.tokens = new ArrayList<>();
	}

	public Stmt(List<Token> tokens) {
		this.line = null;
		this.words = new ArrayList<>();
		this.tokens = tokens;
	}

	public int size() {
		return tokens.size();
	}

	public Token getToken(int index) {
		return tokens.get(index);
	}

	public int indexOf(Token token) {
		return tokens.indexOf(token);
	}

	public String get(int index) {// 修改为从token获取字符串
		return getToken(index).value.toString();
	}

	public String frist() {
		return get(0);
	}

	public String last() {
		return get(size() - 1);
	}

	public int indexOf(String str) {
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && str.equals(token.value)) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(String str) {
		int index = -1;
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && str.equals(token.value)) {
				index = i > index ? i : index;
			}
		}
		return index;
	}

	public Stmt subStmt(int start, int end) {
		// 这里一定要new一个,不然subList返回的是原来集合的一个视图
		return new Stmt(new ArrayList<>(tokens.subList(start, end)));
	}

	public Stmt subStmt(String left, String right) {
		return subStmt(indexOf(left), lastIndexOf(right));
	}

	public List<Stmt> split(String separator) {// 通过分隔符来获取子语句
		List<Stmt> subStmts = new ArrayList<>();
		for (int i = 0, last = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && separator.equals(token.value)) {// 分隔符
				Stmt subStmt = subStmt(last, i);
				subStmts.add(subStmt);
				last = i + 1;// 记录截取开始的地方
			} else if (i == size() - 1) {// 到达最后
				Stmt subStmt = subStmt(last, i + 1);
				subStmts.add(subStmt);
			}
		}
		return subStmts;
	}

	public Stmt replace(int start, int end, Token token) {// 将指定位置之间的token用传入的来替代,返回的是一个拷贝
		List<Token> newTokens = new ArrayList<>();
		for (int i = 0; i < size(); i++) {
			// 注意不再这个范围的才添加
			if (i < start || i >= end) {
				newTokens.add(tokens.get(i));
			} else if (i == start) {
				newTokens.add(token);
			}
		}
		return new Stmt(newTokens);
	}

	@Override
	public String toString() {
		// 如果没有token,则直接返回line
		if (size() == 0)
			return line.text;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			String str = format(i, getToken(i));
			sb.append(str);
		}
		return sb.toString();
	}

	public String format(Token token) {
		return format(indexOf(token), token);
	}

	public String format(int index, Token token) {

		if (token == null)
			return "";

		if (token.isKeyword()) {// 关键字一般后面加个空格
			if ("try".equals(token.value)) {// try语句不动
				return token.value.toString();
			} else if ("else".equals(token.value) && size() == 3) {// } else { 语句
				return token.value.toString();
			} else if ("in".equals(token.value)) {// for xxx in xxx :
				return " " + token.value + " ";
			} else if ("continue".equals(token.value)) {
				return token.value.toString();
			} else if ("break".equals(token.value)) {
				return token.value.toString();
			} else if ("instanceof".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("return".equals(token.value) && size() == 2) {// return;
				return token.value.toString();
			} else if ("extends".equals(token.value)) {// extends
				return " " + token.value + " ";
			} else if ("impl".equals(token.value)) {// impl shy代码显示的更好看点
				return " " + token.value + " ";
			} else {
				return token.value + " ";
			}

		} else if (token.isOperator()) {// 有些操作符两边加空格会好看些
			if ("&&".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("||".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("=".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("==".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("!=".equals(token.value)) {
				return " " + token.value + " ";
			} else if (">".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("<".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("<<".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("+".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("-".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("*".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("/".equals(token.value)) {
				return " " + token.value + " ";
			} else if ("<=".equals(token.value)) {
				return " " + token.value + " ";
			} else if (">=".equals(token.value)) {
				return " " + token.value + " ";
			} else {
				return token.value.toString();
			}
		} else if (token.isSeparator()) {// 末尾的括号前面加个空格
			if (";".equals(token.value)) {// ;在后面加空格
				return token.value + " ";
			} else if (",".equals(token.value)) {// ,后面加空格
				return token.value + " ";
			} else if ("{".equals(token.value) && index == size() - 1) {// 如果{结尾,则在前面加个空格
				return " " + token.value;
			} else if ("}".equals(token.value) && index == 0 && size() != 1) {// 如果}是开头,并且不是孤零零的一个
				return token.value + " ";
			} else if (":".equals(token.value)) {// if xxx==xxx : print xxx
				return " " + token.value + " ";
			} else {
				return token.value.toString();
			}

		} else if (token.isType()) {// 类型声明后面加空格
			if (index - 1 > 0 && "instanceof".equals(get(index - 1))) {// 类型判断语句中类型后面不用加空格 a instanceof List
				return token.value.toString();
			} else if (index + 1 < size() && get(index + 1).startsWith(".")) {// Type.get()后面不用加空格了
				return token.value.toString();
			} else if (syntax != null) {// 泛型里面的类型后面就不用加空格了,判断条件时,子语句是的语法参数是null
				return token.value + " ";
			} else {
				return token.value.toString();
			}
		} else {
			return token.value.toString();
		}

	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			sb.append(token.toString() + " ");
		}
		return sb.toString();
	}

	public boolean isAssign() {
		return Constants.ASSIGN_SYNTAX.equals(syntax);
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

	public boolean isDeclare() {
		return Constants.DECLARE_SYNTAX.equals(syntax);
	}

	public boolean isCatch() {
		return Constants.CATCH_SYNTAX.equals(syntax);
	}

	public boolean isForIn() {
		return Constants.FOR_IN_SYNTAX.equals(syntax);
	}

	public boolean isFor() {
		return Constants.FOR_SYNTAX.equals(syntax);
	}

	public boolean isWhile() {
		return Constants.WHILE_SYNTAX.equals(syntax);
	}

}
