package com.sum.spirit.api;

import java.util.List;

import com.sum.spirit.core.element.entity.Token;

public interface SemanticParser {

	Token getToken(String word, boolean insideType);

	default Token getToken(String word) {
		return getToken(word, false);
	}

	List<Token> getTokens(List<String> words, boolean insideType);

	default List<Token> getTokens(List<String> words) {
		return getTokens(words, false);
	}

	boolean isPrimitive(String word);

	boolean isType(String word);

}
