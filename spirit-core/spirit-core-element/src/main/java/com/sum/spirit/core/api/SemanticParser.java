package com.sum.spirit.core.api;

import java.util.List;

import com.sum.spirit.core.element.entity.SemanticContext;
import com.sum.spirit.core.element.entity.Token;

public interface SemanticParser {

	List<Token> getTokens(SemanticContext context, List<String> words);

	default List<Token> getTokens(List<String> words) {
		return getTokens(new SemanticContext(), words);
	}

	Token getToken(SemanticContext context, String word);

	default Token getToken(String word) {
		return getToken(new SemanticContext(), word);
	}

	boolean isType(String word);

}
