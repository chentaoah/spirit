package com.sum.spirit.core.element.frame;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

public class TokenBox {

	public List<Token> tokens;

	public TokenBox(List<Token> tokens) {
		this.tokens = tokens;
	}

	public boolean matches(Token token) {
		return token.isOperator() || token.isSeparator();
	}

	public int size() {
		return tokens.size();
	}

	public boolean contains(int index) {
		return index < tokens.size();
	}

	public int indexOf(Token token) {
		return tokens.indexOf(token);
	}

	public boolean contains(Token token) {
		return tokens.contains(token);
	}

	public Token getToken(int index) {
		return tokens.get(index);
	}

	public Token lastToken() {
		return tokens.get(tokens.size() - 1);
	}

	public void addToken(Token token) {
		tokens.add(token);
	}

	public void addToken(int index, Token token) {
		tokens.add(index, token);
	}

	public void setToken(int index, Token token) {
		tokens.set(index, token);
	}

	public void removeToken(int index) {
		tokens.remove(index);
	}

	public void removeToken(Token token) {
		tokens.remove(token);
	}

	public List<Token> copyTokens() {
		return new ArrayList<>(tokens);
	}

	public List<Token> subTokens(int start, int end) {
		return new ArrayList<>(tokens.subList(start, end));
	}

	public void replaceTokens(int start, int end, Token token) {
		for (int i = end - 1; i >= start; i--) {
			tokens.remove(i);
		}
		tokens.add(start, token);
	}

	public Token findOneTokenOf(TokenTypeEnum... tokenTypes) {
		for (Token token : tokens) {
			for (TokenTypeEnum type : tokenTypes) {
				if (token.tokenType == type) {
					return token;
				}
			}
		}
		return null;
	}

	public List<TokenBox> splitTokens(String separator) {
		List<TokenBox> tokenBoxs = new ArrayList<>();
		for (int i = 0, last = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if (matches(token) && separator.equals(token.toString())) {
				tokenBoxs.add(new TokenBox(subTokens(last, i)));
				last = i + 1;
			} else if (i == size() - 1) {
				tokenBoxs.add(new TokenBox(subTokens(last, i + 1)));
			}
		}
		return tokenBoxs;
	}

	public int indexOf(String str) {
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if (matches(token) && str.equals(token.toString())) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(String str) {
		int index = -1;
		for (int i = 0; i < size(); i++) {
			Token token = tokens.get(i);
			if (matches(token) && str.equals(token.toString())) {
				index = i > index ? i : index;
			}
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
			if (token.isKeyword() && keyword.equals(token.toString())) {
				return i;
			}
		}
		return -1;
	}

	public boolean containsKeyword(String keyword) {
		return indexOfKeyword(keyword) != -1;
	}

	public void removeKeyword(String keyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			removeToken(index);
		}
	}

	public void replaceKeyword(String keyword, String newKeyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			setToken(index, new Token(TokenTypeEnum.KEYWORD, newKeyword));
		}
	}

	public void addKeywordAtFirst(String keyword) {
		addToken(0, new Token(TokenTypeEnum.KEYWORD, keyword));
	}

	public void insertKeywordAfter(String keyword, String newKeyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			addToken(index + 1, new Token(TokenTypeEnum.KEYWORD, newKeyword));
		}
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
			if (index != -1 && contains(index + 1)) {
				return getToken(index + 1);
			}
		}
		return null;
	}

	public List<Token> getKeywordParams(String keyword) {
		List<Token> params = new ArrayList<>();
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			int end = findKeywordEnd(index);
			List<TokenBox> tokenBoxs = new TokenBox(subTokens(index + 1, end)).splitTokens(",");
			for (TokenBox tokenBox : tokenBoxs) {
				Assert.isTrue(tokenBox.size() == 1, "The size must be 1!");
				params.add(tokenBox.getToken(0));
			}
		}
		return params;
	}

}
