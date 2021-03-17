package com.sum.spirit.core.api;

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

	boolean isPath(String word);

	boolean isAnnotation(String word);

	boolean isKeyword(String word);

	boolean isOperator(String word);

	boolean isSeparator(String word);

	boolean isType(String word);

	boolean isInit(String word);

	boolean isLiteral(String word);

	boolean isSubexpress(String word);

	boolean isVariable(String word);

	boolean isAccess(String word);

}
