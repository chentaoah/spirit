package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.element.Token;

@Service("struct_recognizer")
public interface StructRecognizer {

	String getSyntax(List<Token> tokens);

}
