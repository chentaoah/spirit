package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

public abstract class TokenBox {

	public int size() {
		return getTokens().size();
	}

	public boolean contains(int index) {
		return index < size();
	}

	public boolean isFocusOn(Token token) {
		return token.isOperator() || token.isSeparator();
	}

	public int indexOf(Token token) {
		return getTokens().indexOf(token);
	}

	public boolean contains(Token token) {
		return getTokens().contains(token);
	}

	public Token getToken(int index) {
		return getTokens().get(index);
	}

	public void addToken(Token token) {
		getTokens().add(token);
	}

	public void addToken(int index, Token token) {
		getTokens().add(index, token);
	}

	public void setToken(int index, Token token) {
		getTokens().set(index, token);
	}

	public void removeToken(int index) {
		getTokens().remove(index);
	}

	public void removeToken(Token token) {
		getTokens().remove(token);
	}

	public List<Token> copyTokens() {
		return new ArrayList<>(getTokens());
	}

	public List<Token> subTokens(int start, int end) {
		return new ArrayList<>(getTokens().subList(start, end));
	}

	public void replaceTokens(int start, int end, Token token) {
		List<Token> tokens = getTokens();
		for (int i = end - 1; i >= start; i--)
			tokens.remove(i);
		tokens.add(start, token);
	}

	public Token findToken(TokenTypeEnum... tokenTypes) {
		for (Token token : getTokens()) {
			for (TokenTypeEnum type : tokenTypes) {
				if (token.type == type)
					return token;
			}
		}
		return null;
	}

	public List<List<Token>> splitTokens(String separator) {
		List<List<Token>> tokensList = new ArrayList<>();
		for (int i = 0, last = 0; i < size(); i++) {
			Token token = getTokens().get(i);
			if (isFocusOn(token) && separator.equals(token.toString())) {
				tokensList.add(subTokens(last, i));
				last = i + 1;
			} else if (i == size() - 1) {
				tokensList.add(subTokens(last, i + 1));
			}
		}
		return tokensList;
	}

	public int indexOf(String str) {
		List<Token> tokens = getTokens();
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if (isFocusOn(token) && str.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public int lastIndexOf(String str) {
		int index = -1;
		List<Token> tokens = getTokens();
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if (isFocusOn(token) && str.equals(token.toString()))
				index = i > index ? i : index;
		}
		return index;
	}

	public boolean contains(String str) {
		return indexOf(str) >= 0;
	}

	public String getStr(int index) {
		return getToken(index).toString();
	}

	public String first() {
		return getStr(0);
	}

	public String last() {
		return getStr(size() - 1);
	}

	public int indexOfKeyword(String keyword) {
		for (int i = 0; i < size(); i++) {
			Token token = getToken(i);
			if (token.isKeyword() && keyword.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public boolean containsKeyword(String keyword) {
		return indexOfKeyword(keyword) != -1;
	}

	public void removeKeyword(String keyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1)
			removeToken(index);
	}

	public void replaceKeyword(String keyword, String newKeyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1)
			setToken(index, new Token(TokenTypeEnum.KEYWORD, newKeyword));
	}

	public void addKeywordAtFirst(String keyword) {
		addToken(0, new Token(TokenTypeEnum.KEYWORD, keyword));
	}

	public void insertKeywordAfter(String keyword, String newKeyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1)
			addToken(index + 1, new Token(TokenTypeEnum.KEYWORD, newKeyword));
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
			int index = indexOfKeyword(keyword);
			if (index != -1 && contains(index + 1))
				return getToken(index + 1);
		}
		return null;
	}

	public List<Token> getKeywordParams(String keyword) {
		List<Token> params = new ArrayList<>();
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			int end = findKeywordEnd(index);
			List<List<Token>> tokensList = new DefaultTokenBox(subTokens(index + 1, end)).splitTokens(",");
			for (List<Token> tokens : tokensList) {
				Assert.isTrue(tokens.size() == 1, "The size must be 1!");
				params.add(tokens.get(0));
			}
		}
		return params;
	}

	public abstract List<Token> getTokens();

	public static class DefaultTokenBox extends TokenBox {

		public List<Token> tokens;

		public DefaultTokenBox(List<Token> tokens) {
			this.tokens = tokens;
		}

		@Override
		public List<Token> getTokens() {
			return tokens;
		}
	}
}
