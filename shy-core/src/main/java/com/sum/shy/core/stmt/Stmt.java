package com.sum.shy.core.stmt;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Symbol;
import com.sum.shy.core.stmt.api.TokenBox;

public class Stmt extends TokenBox {

	public List<Token> tokens;

	public Stmt(List<Token> tokens) {
		this.tokens = tokens;
	}

	public Stmt copy() {// 拷贝一份新的tokens
		return new Stmt(copyTokens());
	}

	public Stmt subStmt(int start, int end) {// 这里一定要new一个,不然subList返回的是原来集合的一个视图
		return new Stmt(subTokens(start, end));
	}

	public Stmt subStmt(String left, String right) {
		return subStmt(indexOf(left) + 1, lastIndexOf(right));
	}

	public List<Stmt> split(String separator) {// 通过分隔符来获取子语句
		List<Stmt> subStmts = new ArrayList<>();
		List<List<Token>> tokensList = splitTokens(separator);
		for (List<Token> tokens : tokensList)
			subStmts.add(new Stmt(tokens));
		return subStmts;
	}

	@Override
	public List<Token> getTokens() {
		return tokens;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Token> tokens = format();
		for (int i = 0; i < tokens.size(); i++)
			sb.append(tokens.get(i));
		return sb.toString();
	}

	public List<Token> format() {
		// 拷贝一份
		List<Token> tokens = copyTokens();
		// 并插入空格
		for (int i = tokens.size() - 1; i >= 1; i--)
			tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));
		// 遍历空格
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.isSeparator() && " ".equals(token.toString())) {
				Token lastToken = tokens.get(i - 1);
				Token nextToken = tokens.get(i + 1);
				if (lastToken.isOperator()) {// 前面为某些特定操作符
					if ("!".equals(lastToken.toString())) {
						tokens.remove(i);
						continue;

					} else if ("++".equals(lastToken.toString()) || "--".equals(lastToken.toString())) {
						if (lastToken.getOperand() == Symbol.RIGHT) {
							tokens.remove(i);
							continue;
						}

					} else if ("-".equals(lastToken.toString())) {
						if (lastToken.getOperand() == Symbol.RIGHT) {
							tokens.remove(i);
							continue;
						}
					}

				} else if (lastToken.isSeparator()) {// 前面为特定分隔符
					if ("[".equals(lastToken.toString()) || "(".equals(lastToken.toString())
							|| "<".equals(lastToken.toString())) {
						tokens.remove(i);
						continue;
					}

				} else if (lastToken.isCustomPrefix()) {
					tokens.remove(i);
					continue;
				}

				if (nextToken.isOperator()) {
					if ("++".equals(nextToken.toString()) || "--".equals(nextToken.toString())) {
						if (nextToken.getOperand() == Symbol.LEFT) {
							tokens.remove(i);
							continue;
						}
					}

				} else if (nextToken.isSeparator()) {
					if ("[".equals(nextToken.toString()) || "(".equals(nextToken.toString())
							|| "<".equals(nextToken.toString()) || "]".equals(nextToken.toString())
							|| ")".equals(nextToken.toString()) || ">".equals(nextToken.toString())
							|| ",".equals(nextToken.toString()) || ";".equals(nextToken.toString())) {

						if (lastToken.isKeyword() && "(".equals(nextToken.toString())) {
							continue;// if (express) {

						} else {
							tokens.remove(i);
							continue;
						}
					}

				} else if (nextToken.isFluent()) {
					tokens.remove(i);
					continue;

				} else if (nextToken.isCustomSuffix()) {
					tokens.remove(i);
					continue;
				}
			}
		}
		return tokens;
	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : getTokens())
			sb.append(token.debug() + " ");
		return sb.toString().trim();
	}

}
