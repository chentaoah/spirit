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

	public Token findToken(String... types) {
		for (Token token : tokens) {
			for (String type : types) {
				if (token.type.equals(type))
					return token;
			}
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
		// 拷贝一份
		List<Token> tokens = new ArrayList<>(this.tokens);
		// 并插入空格
		for (int i = tokens.size() - 1; i >= 1; i--)
			tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));
		// 遍历空格
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.isSeparator()) {
				if (" ".equals(token.toString())) {
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
						if ("[".equals(lastToken.toString()) || "{".equals(lastToken.toString())
								|| "(".equals(lastToken.toString()) || "<".equals(lastToken.toString())) {
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
						if ("[".equals(nextToken.toString()) || "{".equals(nextToken.toString())
								|| "(".equals(nextToken.toString()) || "<".equals(nextToken.toString())
								|| "]".equals(nextToken.toString()) || "}".equals(nextToken.toString())
								|| ")".equals(nextToken.toString()) || ">".equals(nextToken.toString())
								|| ",".equals(nextToken.toString()) || ";".equals(nextToken.toString())) {

							if (lastToken.isKeyword() && "(".equals(nextToken.toString())) {
								continue;// if (express) {

							} else if (i + 1 == tokens.size() - 1 && "{".equals(nextToken.toString())) {
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
		}

		return tokens;

	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens)
			sb.append(token.debug() + " ");
		return sb.toString().trim();
	}

}
