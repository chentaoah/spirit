package com.sum.spirit.core.element.frame;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.common.entity.MappableList;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.common.utils.Splitter;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;

public class TokenBox extends MappableList<Token> {

	public TokenBox(List<Token> tokens) {
		super(tokens);
	}

	public boolean contains(int index) {
		return index >= 0 && index < size();
	}

	public Token firstToken() {
		return get(0);
	}

	public Token lastToken() {
		return get(size() - 1);
	}

	public List<Token> copyTokens() {
		return new ArrayList<>(this);
	}

	public List<Token> subTokens(int fromIndex, int toIndex) {
		return new ArrayList<>(subList(fromIndex, toIndex));
	}

	public void replaceTokens(int fromIndex, int toIndex, Token token) {
		Lists.remove(this, fromIndex, toIndex);
		add(fromIndex, token);
	}

	public Token findOneTokenOf(TokenTypeEnum... tokenTypes) {
		return CollUtil.findOne(this, token -> ArrayUtil.contains(tokenTypes, token.tokenType));
	}

	public boolean isSymbol(Token token) {
		return token.isOperator() || token.isSeparator();
	}

	public List<TokenBox> splitTokens(String separator) {
		return Splitter.splitByMatcherTrim(this, token -> isSymbol(token) && separator.equals(token.toString()), list -> new TokenBox(list));
	}

	public int indexOf(String str) {
		return Lists.indexOf(this, token -> isSymbol(token) && str.equals(token.toString()));
	}

	public int lastIndexOf(String str) {
		return Lists.lastIndexOf(this, token -> isSymbol(token) && str.equals(token.toString()));
	}

	public boolean contains(String str) {
		return indexOf(str) >= 0;
	}

	public String getStr(int index) {
		return get(index).toString();
	}

	public String first() {
		return getStr(0);
	}

	public String last() {
		return getStr(size() - 1);
	}

}
