package com.sum.spirit.api;

import java.util.List;

public interface Lexer {

	List<String> getWords(String text);

	List<String> getSubWords(String text, Character... splitChars);

}
