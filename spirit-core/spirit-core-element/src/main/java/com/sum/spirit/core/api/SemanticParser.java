package com.sum.spirit.core.api;

import java.util.List;

import com.sum.spirit.core.element.entity.Token;

public interface SemanticParser {

	List<Token> getTokens(List<String> words);

	List<Token> getTokensInsideType(List<String> words);

	Token getToken(String word);

	Token getTokenInsideType(String word);

	boolean isPath(String word);

	boolean isAnnotation(String word);

	boolean isKeyword(String word);

	boolean isOperator(String word);

	boolean isSeparator(String word);

	boolean isType(String word);

	boolean isVariable(String word);

}
