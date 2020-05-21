package com.sum.shy.core.stmt.api;

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
			if (isMatch(token) && str.equals(token.toString())) {
				index = i > index ? i : index;
			}
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

	public void replace(int start, int end, Token token) {
		List<Token> tokens = getTokens();
		for (int i = end - 1; i >= start; i--)
			tokens.remove(i);
		tokens.add(start, token);
	}

	public abstract List<Token> getTokens();

}
