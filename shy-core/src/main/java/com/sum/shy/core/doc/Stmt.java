package com.sum.shy.core.doc;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Constants;

public class Stmt {

	public List<Token> tokens;

	public Stmt(List<Token> tokens) {
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
		return getToken(index).toString();
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

	public List<Node> findNodes() {
		List<Node> nodes = new ArrayList<>();
		for (Token token : tokens) {
			if (token.isNode())
				nodes.add(token.getNode());
		}
		return nodes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// 1.插入空格
		List<Token> tokens = new ArrayList<>(this.tokens);
		for (int i = tokens.size() - 1; i >= 0; i--) {
			Token token = tokens.get(i);
			if (token.isSeparator()) {
				if ("[".equals(token.toString()) || "{".equals(token.toString()) || "(".equals(token.toString())
						|| "<".equals(token.toString())) {
					if (i + 1 < tokens.size()) {
						Token nextToken = tokens.get(i + 1);
						if (nextToken.isSeparator() && " ".equals(nextToken.toString()))
							tokens.remove(i + 1);
					}
				}
			} else {
				if (i != 0)
					tokens.add(i, new Token(Constants.SEPARATOR_TOKEN, " "));
			}
		}
		// 2.开始拼接字符串
		for (int i = 0; i < tokens.size(); i++)
			sb.append(tokens.get(i));

		return sb.toString();
	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens)
			sb.append(token.debug() + " ");
		return sb.toString().trim();
	}

}
