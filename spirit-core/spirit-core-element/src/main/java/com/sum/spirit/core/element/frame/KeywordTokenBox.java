package com.sum.spirit.core.element.frame;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

public abstract class KeywordTokenBox extends TokenBox {

	public KeywordTokenBox(List<Token> tokens) {
		super(tokens);
	}

	public int indexOfKeyword(String keyword) {
		return ListUtils.indexOf(this, token -> token.isKeyword() && keyword.equals(token.toString()));
	}

	public boolean containsKeyword(String keyword) {
		return indexOfKeyword(keyword) != -1;
	}

	public void removeKeyword(String keyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			remove(index);
		}
	}

	public void replaceKeyword(String keyword, String newKeyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			set(index, new Token(TokenTypeEnum.KEYWORD, newKeyword));
		}
	}

	public void addKeywordAtFirst(String keyword) {
		add(0, new Token(TokenTypeEnum.KEYWORD, keyword));
	}

	public void addKeyword(String keyword) {
		add(new Token(TokenTypeEnum.KEYWORD, keyword));
	}

	public void insertKeywordAfter(String keyword, String newKeyword) {
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			add(index + 1, new Token(TokenTypeEnum.KEYWORD, newKeyword));
		}
	}

	public int findKeywordEnd(int index) {
		int endIndex = ListUtils.indexOf(this, index + 1, token -> token.isKeyword() || (token.isSeparator() && !",".equals(token.toString())));
		if (endIndex == -1) {
			return size();
		}
		return endIndex + 1;
	}

	public Token getKeywordParam(String... keywords) {
		for (String keyword : keywords) {
			int index = indexOfKeyword(keyword);
			if (index != -1 && contains(index + 1)) {
				return get(index + 1);
			}
		}
		return null;
	}

	public List<Token> getKeywordParams(String keyword) {
		List<Token> params = new ArrayList<>();
		int index = indexOfKeyword(keyword);
		if (index != -1) {
			int endIndex = findKeywordEnd(index);
			List<TokenBox> tokenBoxs = new TokenBox(subTokens(index + 1, endIndex)).splitTokens(",");
			for (TokenBox tokenBox : tokenBoxs) {
				Assert.isTrue(tokenBox.size() == 1, "The size must be 1!");
				params.add(tokenBox.get(0));
			}
		}
		return params;
	}
}
