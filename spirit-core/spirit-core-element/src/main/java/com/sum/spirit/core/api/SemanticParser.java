package com.sum.spirit.core.api;

import java.util.List;

import com.sum.spirit.core.element.entity.SemanticContext;
import com.sum.spirit.core.element.entity.Token;

public interface SemanticParser {

	List<Token> getTokens(SemanticContext context, List<String> words);

	Token getToken(SemanticContext context, String word);

	boolean isType(String word);

}
