package com.sum.jass.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.element.Token;

@Service("struct_recognizer")
public interface StructRecognizer {

	String getSyntax(List<Token> tokens);

}
