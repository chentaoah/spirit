package com.sum.shy.api.lexer;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Token;

@Service("semantic_parser")
public interface SemanticParser {

	default List<Token> getTokens(List<String> words) {
		List<Token> tokens = new ArrayList<>();
		for (String word : words)
			tokens.add(getToken(word));
		return tokens;
	}

	Token getToken(String word);

}