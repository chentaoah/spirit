package com.sum.shy.core.document;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.metadata.Symbol;

public class Stmt {

	public List<Token> tokens;

	public Stmt(List<Token> tokens) {
		this.tokens = tokens;
	}

	public Stmt copy() {
		return new Stmt(tokens);
	}

	public int size() {
		return tokens.size();
	}

	public Token getToken(int index) {
		return tokens.get(index);
	}

	public void addToken(int index, Token token) {
		tokens.add(index, token);
	}

	public void addToken(Token token) {
		tokens.add(token);
	}

	public void setToken(int index, Token token) {
		tokens.set(index, token);
	}

	public String last() {
		return getStr(size() - 1);
	}

	public Token findToken(String type) {
		for (Token token : tokens) {
			if (token.type.equals(type))
				return token;
		}
		return null;
	}

	public String getStr(int index) {// 修改为从token获取字符串
		return getToken(index).toString();
	}

	public int indexOf(Token token) {
		return tokens.indexOf(token);
	}

	public boolean contains(String str) {
		return indexOf(str) >= 0;
	}

	public int indexOf(String str) {
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && str.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public int lastIndexOf(String str) {
		int index = -1;
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && str.equals(token.toString())) {
				index = i > index ? i : index;
			}
		}
		return index;
	}

	public Stmt subStmt(int start, int end) {// 这里一定要new一个,不然subList返回的是原来集合的一个视图
		return new Stmt(new ArrayList<>(tokens.subList(start, end)));
	}

	public Stmt subStmt(String left, String right) {
		return subStmt(indexOf(left) + 1, lastIndexOf(right));
	}

	public List<Stmt> split(String separator) {// 通过分隔符来获取子语句
		List<Stmt> subStmts = new ArrayList<>();
		for (int i = 0, last = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if ((token.isSeparator() || token.isOperator()) && separator.equals(token.toString())) {// 分隔符
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

	public void replace(int start, int end, Token token) {
		for (int i = end - 1; i >= start; i--)
			tokens.remove(i);
		tokens.add(start, token);
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
		// 拷贝一份，并插入空格
		List<Token> tokens = new ArrayList<>(this.tokens);
		// 首个token前面不用插入
		for (int i = tokens.size() - 1; i >= 0; i--) {
			Token token = tokens.get(i);
			if (token.isSeparator()) {
				if ("[".equals(token.toString()) || "{".equals(token.toString()) || "(".equals(token.toString())
						|| "<".equals(token.toString())) {

					removeAfterSpace(tokens, i);

					if ("(".equals(token.toString()) && i - 1 >= 0) {// 如果前面是关键字，则添加个括号，if (expression) {
						Token lastToken = tokens.get(i - 1);
						if (lastToken.isKeyword())
							tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));

					} else if ("{".equals(token.toString())) {// 最后一个前面加空格
						if (i == tokens.size() - 1)
							tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));
					}

				} else if (":".equals(token.toString())) {
					tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));
				}

			} else if (token.isOperator()) {
				if ("!".equals(token.toString())) {
					removeAfterSpace(tokens, i);

				} else if ("++".equals(token.toString()) || "--".equals(token.toString())) {
					if (token.getOperand() == Symbol.LEFT) {
						continue;
					} else if (token.getOperand() == Symbol.RIGHT) {
						removeAfterSpace(tokens, i);
					}

				} else if ("-".equals(token.toString())) {
					if (token.getOperand() == Symbol.RIGHT) {
						removeAfterSpace(tokens, i);
					}
					tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));

				} else {
					tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));
				}

			} else if (token.isCustomPrefix()) {
				removeAfterSpace(tokens, i);

			} else if (token.isFluent() || token.isCustomSuffix()) {// 属性访问，则什么都不做
				continue;

			} else if (i >= 1) {// 第一个前面不用加空格
				tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));
			}

		}
		return tokens;
	}

	public void removeAfterSpace(List<Token> tokens, int index) {
		if (index + 1 < tokens.size()) {// 删除后面多余的空格
			Token nextToken = tokens.get(index + 1);
			if (nextToken.isSeparator() && " ".equals(nextToken.toString()))
				tokens.remove(index + 1);
		}
	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens)
			sb.append(token.debug() + " ");
		return sb.toString().trim();
	}

}
