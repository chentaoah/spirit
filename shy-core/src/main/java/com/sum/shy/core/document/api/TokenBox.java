package com.sum.shy.core.document.api;

import java.util.List;

import com.sum.shy.core.document.Token;

public abstract class TokenBox {

	public int size() {
		return getTokens().size();
	}

	public int indexOf(Token token) {
		return getTokens().indexOf(token);
	}

	public boolean isHandler(Token token) {
		return token.isSeparator() || token.isOperator();
	}

	public int indexOf(String str) {
		for (int i = 0; i < size(); i++) {
			Token token = getTokens().get(i);
			if (isHandler(token) && str.equals(token.toString()))
				return i;
		}
		return -1;
	}

	public int lastIndexOf(String str) {
		int index = -1;
		for (int i = 0; i < size(); i++) {
			Token token = getTokens().get(i);
			if (isHandler(token) && str.equals(token.toString())) {
				index = i > index ? i : index;
			}
		}
		return index;
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
		for (int i = end - 1; i >= start; i--)
			getTokens().remove(i);
		getTokens().add(start, token);
	}

	public abstract List<Token> getTokens();

}
