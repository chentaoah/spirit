package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.spirit.pojo.element.Token;

public interface StructRecognizer {

	String getSyntax(List<Token> tokens);

}
