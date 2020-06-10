package com.sum.shy.api.lexer;

import java.util.List;

import com.sum.pisces.api.Service;

@Service("lexer")
public interface Lexer {

	List<String> getWords(String text);

}
