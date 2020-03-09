package com.sum.shy.core.document;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.lexical.LexicalAnalyzer;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.lexical.StructRecognizer;
import com.sum.shy.core.lexical.TreeBuilder;
import com.sum.shy.core.utils.LineUtils;

@SuppressWarnings("serial")
public class Element extends ArrayList<Element> {
	// 行
	public Line line;
	// 语句
	public Stmt stmt;
	// 语法树
	public Tree tree;
	// 语法
	public String syntax;

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
			// 4.建立抽象语法树
			this.tree = TreeBuilder.build(stmt);
			// 5.获取语法
			this.syntax = tree.getSyntax();
		}

	}

	public boolean hasChild() {
		return line.text.trim().endsWith("{");
	}

	public String getStr(int index) {
		return stmt.getStr(index);
	}

	public int getSize() {
		return stmt.size();
	}

	public Stmt subStmt(int start, int end) {
		return stmt.subStmt(start, end);
	}

	public Token findToken(String type) {
		return stmt.findToken(type);
	}

	public Token getToken(int index) {
		return stmt.getToken(index);
	}

	public boolean contains(int index) {
		return index < stmt.size();
	}

	public int indexOf(String str) {
		return stmt.indexOf(str);
	}

	public boolean contains(String str) {
		return indexOf(str) >= 0;
	}

	public int lastIndexOf(String str) {
		return stmt.lastIndexOf(str);
	}

	public List<Stmt> split(String separator) {
		return stmt.split(separator);
	}

	public String getKeywordParam(String keyword) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.tokens.get(i);
			if (token.isKeyword() && keyword.equals(token.toString())) {
				if (i + 1 < stmt.size()) {
					Token nextToken = stmt.tokens.get(i + 1);
					return nextToken.toString();
				}
			}
		}
		return null;
	}

	public List<String> getKeywordParams(String keyword) {
		List<String> params = new ArrayList<>();
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.tokens.get(i);
			if (token.isKeyword() && keyword.equals(token.toString())) {
				int end = -1;
				for (int j = i + 1; j < stmt.size(); j++) {// 查询到结束的位置
					Token endToken = stmt.tokens.get(j);
					if (endToken.isKeyword() || (endToken.isSeparator() && !",".equals(endToken.toString()))) {
						end = j;
					} else if (j == stmt.size() - 1) {
						end = j + 1;
					}
				}
				if (end != -1) {
					List<Stmt> subStmts = stmt.subStmt(i + 1, end).split(",");
					for (Stmt subStmt : subStmts) {
						if (subStmt.size() == 1)
							params.add(subStmt.getStr(0));
					}
					return params;
				}
			}
		}
		return null;
	}

	public void debug() {
		System.out.println(line.text + LineUtils.getSpaceByNumber(100 - line.text.length()) + ">>> " + syntax + " "
				+ stmt.debug());
		for (Element element : this)
			element.debug();
	}

	@Override
	public String toString() {
		return stmt.toString();
	}

	public boolean isImport() {
		return Constants.IMPORT_SYNTAX.equals(syntax);
	}

	public boolean isAnnotation() {
		return Constants.ANNOTATION_SYNTAX.equals(syntax);
	}

	public boolean isInterface() {
		return Constants.INTERFACE_SYNTAX.equals(syntax);
	}

	public boolean isAbstract() {
		return Constants.ABSTRACT_SYNTAX.equals(syntax);
	}

	public boolean isClass() {
		return Constants.CLASS_SYNTAX.equals(syntax);
	}

	public boolean isDeclare() {
		return Constants.DECLARE_SYNTAX.equals(syntax);
	}

	public boolean isDeclareAssign() {
		return Constants.DECLARE_ASSIGN_SYNTAX.equals(syntax);
	}

	public boolean isAssign() {
		return Constants.ASSIGN_SYNTAX.equals(syntax);
	}

	public boolean isFuncDeclare() {
		return Constants.FUNC_DECLARE_SYNTAX.equals(syntax);
	}

	public boolean isFunc() {
		return Constants.FUNC_SYNTAX.equals(syntax);
	}

	public boolean isSuper() {
		return Constants.SUPER_SYNTAX.equals(syntax);
	}

	public boolean isThis() {
		return Constants.THIS_SYNTAX.equals(syntax);
	}

	public boolean isFieldAssign() {
		return Constants.FIELD_ASSIGN_SYNTAX.equals(syntax);
	}

	public boolean isReturn() {
		return Constants.RETURN_SYNTAX.equals(syntax);
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

	public boolean isForIn() {
		return Constants.FOR_IN_SYNTAX.equals(syntax);
	}

	public boolean isFor() {
		return Constants.FOR_SYNTAX.equals(syntax);
	}

	public boolean isWhile() {
		return Constants.WHILE_SYNTAX.equals(syntax);
	}

	public boolean isTry() {
		return Constants.TRY_SYNTAX.equals(syntax);
	}

	public boolean isCatch() {
		return Constants.CATCH_SYNTAX.equals(syntax);
	}

	public boolean isFinally() {
		return Constants.FINALLY_SYNTAX.equals(syntax);
	}

	public boolean isSync() {
		return Constants.SYNC_SYNTAX.equals(syntax);
	}

	public boolean isInvoke() {
		return Constants.INVOKE_SYNTAX.equals(syntax);
	}

	public boolean isContinue() {
		return Constants.CONTINUE_SYNTAX.equals(syntax);
	}

	public boolean isBreak() {
		return Constants.BREAK_SYNTAX.equals(syntax);
	}

	public boolean isThrow() {
		return Constants.THROW_SYNTAX.equals(syntax);
	}

}
