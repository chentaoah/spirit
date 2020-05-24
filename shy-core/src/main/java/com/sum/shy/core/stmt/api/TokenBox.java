package com.sum.shy.core.stmt.api;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.stmt.Token;
import com.sum.shy.lib.Assert;

public abstract class TokenBox {

	public int size() {
		return getTokens().size();
	}

	public int indexOf(Token token) {
		return getTokens().indexOf(token);
	}

	public int indexOf(String str) {
		Assert.notEmpty(str, "Str cannot be empty!");
		List<Token> tokens = getTokens();
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if (isMatch(token) && str.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public int lastIndexOf(String str) {
		Assert.notEmpty(str, "Str cannot be empty!");
		int index = -1;
		List<Token> tokens = getTokens();
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if (isMatch(token) && str.equals(token.toString()))
				index = i > index ? i : index;
		}
		return index;
	}

	public boolean isMatch(Token token) {
		return token.isSeparator() || token.isOperator();
	}

	public boolean contains(int index) {
		return index < size();
	}

	public boolean contains(String str) {
		return indexOf(str) >= 0;
	}

	public Token getToken(int index) {
		return getTokens().get(index);
	}

	public Token findToken(String... types) {
		for (Token token : getTokens()) {
			Assert.notEmpty(token.type, "Token type cannot be empty!");
			for (String type : types) {
				if (token.type.equals(type))
					return token;
			}
		}
		return null;
	}

	public String last() {
		return getStr(size() - 1);
	}

	public String getStr(int index) {// 修改为从token获取字符串
		return getToken(index).toString();
	}

	public void addToken(int index, Token token) {
		getTokens().add(index, token);
	}

	public void addToken(Token token) {
		getTokens().add(token);
	}

	public void setToken(int index, Token token) {
		getTokens().set(index, token);
	}

	public List<Token> copyTokens() {
		return new ArrayList<>(getTokens());
	}

	public List<Token> subTokens(int start, int end) {
		return new ArrayList<>(getTokens().subList(start, end));
	}

	public List<List<Token>> splitTokens(String separator) {
		List<List<Token>> tokensList = new ArrayList<>();
		for (int i = 0, last = 0; i < size(); i++) {
			Token token = getTokens().get(i);
			if (isMatch(token) && separator.equals(token.toString())) {
				tokensList.add(subTokens(last, i));
				last = i + 1;
			} else if (i == size() - 1) {
				tokensList.add(subTokens(last, i + 1));
			}
		}
		return tokensList;
	}

	public void replace(int start, int end, Token token) {
		List<Token> tokens = getTokens();
		for (int i = end - 1; i >= start; i--)
			tokens.remove(i);
		tokens.add(start, token);
	}

	public int findKeyword(String keyword) {
		for (int i = 0; i < size(); i++) {
			Token token = getToken(i);
			if (token.isKeyword() && keyword.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public boolean containsKeyword(String keyword) {
		return findKeyword(keyword) != -1;
	}

	public int findKeywordEnd(int index) {
		for (int i = index + 1; i < size(); i++) {
			Token token = getToken(i);
			if (token.isKeyword() || (token.isSeparator() && !",".equals(token.toString()))) {
				return i;
			} else if (i == size() - 1) {
				return i + 1;
			}
		}
		return -1;
	}

	public Token getKeywordParam(String... keywords) {
		for (String keyword : keywords) {
			int index = findKeyword(keyword);
			if (index != -1 && contains(index + 1))
				return getToken(index + 1);
		}
		return null;
	}

	public List<Token> getKeywordParams(String keyword) {
		List<Token> params = new ArrayList<>();
		int index = findKeyword(keyword);
		if (index != -1) {
			int end = findKeywordEnd(index);
			List<List<Token>> tokensList = new DefaultBox(subTokens(index + 1, end)).splitTokens(",");
			for (List<Token> tokens : tokensList) {
				Assert.isTrue(tokens.size() == 1, "The size must be 1!");
				params.add(tokens.get(0));
			}
		}
		return params;
	}

	public abstract List<Token> getTokens();

	public static class DefaultBox extends TokenBox {

		public List<Token> tokens;

		public DefaultBox(List<Token> tokens) {
			this.tokens = tokens;
		}

		@Override
		public List<Token> getTokens() {
			return tokens;
		}

	}

}
