package com.sum.shy.core.stmt;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.lexical.LexicalAnalyzer;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.lexical.StructRecognizer;
import com.sum.shy.core.lexical.TreeBuilder;
import com.sum.shy.core.stmt.api.Syntactic;
import com.sum.shy.core.utils.LineUtils;
import com.sum.shy.lib.StringUtils;

public class Element extends Syntactic {
	// 行
	public Line line;
	// 语句
	public Stmt stmt;
	// 语法树
	public Tree tree;
	// 语法
	public String syntax;
	// 子节点
	public List<Element> children = new ArrayList<>();

	public Element(String text) {
		this(new Line(text));
	}

	public Element(Line line) {
		this.line = line;
		init(line);
	}

	public void init(Line line) {
		// 1.词法拆分
		List<String> words = LexicalAnalyzer.getWords(line.text);
		// 2.token流
		List<Token> tokens = SemanticDelegate.getTokens(words);
		// 3.生成语句
		this.stmt = new Stmt(tokens);
		// 4.一些基本的结构语法，不需要复杂分析的
		String syntax = StructRecognizer.getStructSyntax(tokens);
		if (syntax != null) {
			this.syntax = syntax;
		} else {
			// 5.建立抽象语法树
			this.tree = TreeBuilder.build(stmt);
			// 6.获取语法
			this.syntax = tree.getSyntax();
		}

	}

	public boolean hasChild() {
		return line.hasChild();
	}

	public String getIndent() {
		return line.getIndent();
	}

	public Stmt subStmt(int start, int end) {
		return stmt.subStmt(start, end);
	}

	public List<Stmt> split(String separator) {
		return stmt.split(separator);
	}

	public int findKeywordIndex(String keyword) {
		for (int i = 0; i < size(); i++) {
			Token token = getToken(i);
			if (token.isKeyword() && keyword.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public boolean containsKeyword(String keyword) {
		return findKeywordIndex(keyword) != -1;
	}

	public Element replaceKeyword(String keyword, String text) {
		int index = findKeywordIndex(keyword);
		if (index != -1) {
			if (StringUtils.isNotEmpty(text)) {
				stmt.tokens.set(index, new Token(Constants.KEYWORD_TOKEN, text));
			} else {
				stmt.tokens.remove(index);// 如果为空的话，则删除该关键字
			}
		}
		return this;
	}

	public Element removeKeyword(String keyword) {
		return replaceKeyword(keyword, "");
	}

	public Element insertAfterKeyword(String keyword, String text) {
		int index = findKeywordIndex(keyword);
		if (index != -1)
			stmt.tokens.add(index + 1, new Token(Constants.KEYWORD_TOKEN, text));
		return this;
	}

	public String getKeywordParam(String... keywords) {
		for (String keyword : keywords) {
			int index = findKeywordIndex(keyword);
			if (index != -1 && contains(index + 1))
				return getToken(index + 1).toString();
		}
		return null;
	}

	public List<String> getKeywordParams(String keyword) {
		List<String> params = new ArrayList<>();
		int index = findKeywordIndex(keyword);
		if (index != -1) {
			int end = -1;
			for (int i = index + 1; i < size(); i++) {// 查询到结束的位置
				Token endToken = getToken(i);
				if (endToken.isKeyword() || (endToken.isSeparator() && !",".equals(endToken.toString()))) {
					end = i;
				} else if (i == size() - 1) {
					end = i + 1;
				}
			}
			if (end != -1) {
				List<Stmt> subStmts = stmt.subStmt(index + 1, end).split(",");
				for (Stmt subStmt : subStmts) {
					if (subStmt.size() == 1)
						params.add(subStmt.getStr(0));
				}
				return params;
			}
		}
		return params;
	}

	@Override
	public List<Token> getTokens() {
		return stmt.tokens;
	}

	@Override
	public String getSyntax() {
		return syntax;
	}

	@Override
	public String toString() {
		return stmt.toString();
	}

	public void debug() {
		System.out.println(
				line.text + LineUtils.getSpace(100 - line.text.length()) + ">>> " + syntax + " " + stmt.debug());
		for (Element element : children)
			element.debug();
	}

}