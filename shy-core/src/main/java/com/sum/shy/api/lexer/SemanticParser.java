package com.sum.shy.api.lexer;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Token;

@Service("semantic_parser")
public interface SemanticParser {

	List<Token> getTokens(List<String> words);

	Token getToken(String word);

}
