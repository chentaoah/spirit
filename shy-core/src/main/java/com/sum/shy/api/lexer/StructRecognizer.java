package com.sum.shy.api.lexer;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Token;

@Service("struct_recognizer")
public interface StructRecognizer {

	String getSyntax(List<Token> tokens);

}
