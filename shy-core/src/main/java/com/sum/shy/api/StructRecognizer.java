package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Token;

@Service("structRecognizer")
public interface StructRecognizer {

	public String getSyntax(List<Token> tokens);

}
