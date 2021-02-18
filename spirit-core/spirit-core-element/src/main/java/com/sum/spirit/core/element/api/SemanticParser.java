package com.sum.spirit.core.element.api;

import java.util.List;

import com.sum.spirit.core.element.entity.Token;

public interface SemanticParser {

	List<Token> getTokens(List<String> words, boolean insideType);

	default List<Token> getTokens(List<String> words) {
		return getTokens(words, false);
	}

	Token getToken(String word, boolean insideType);

	default Token getToken(String word) {
		return getToken(word, false);
	}

	boolean isPrimitive(String word);

	boolean isType(String word);

}
