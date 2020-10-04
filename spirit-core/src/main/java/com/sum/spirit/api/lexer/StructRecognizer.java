package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.SyntaxEnum;

public interface StructRecognizer {

	SyntaxEnum getSyntax(List<Token> tokens);

}
