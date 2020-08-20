package com.sum.spirit.api.lexer;

import java.util.List;

import com.sum.pisces.api.annotation.Service;

@Service("lexer")
public interface Lexer {

	List<String> getWords(String text, Character... excludes);

}
