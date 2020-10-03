package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.spirit.pojo.common.SyntaxEnum;
import com.sum.spirit.pojo.element.Token;

public interface StructRecognizer {

	SyntaxEnum getSyntax(List<Token> tokens);

}
