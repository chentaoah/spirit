package com.sum.spirit.api.lexer;

import java.util.List;

public interface Lexer {

	List<String> getWords(String text, Character... ignoreOnceChars);

}
