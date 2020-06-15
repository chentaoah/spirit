package com.sum.shy.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.element.Token;

@Service("struct_recognizer")
public interface StructRecognizer {

	String getSyntax(List<Token> tokens);

}
