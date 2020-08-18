package com.sum.soon.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.element.Token;

@Service("struct_recognizer")
public interface StructRecognizer {

	String getSyntax(List<Token> tokens);

}
