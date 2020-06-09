package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Token;

@Service("semanticParser")
public interface SemanticParser {

	List<Token> getTokens(List<String> words);

	Token getToken(String word);

}
