package com.sum.shy.api.service.lexer;

import java.util.List;

import com.sum.shy.api.lexer.StructRecognizer;
import com.sum.shy.common.Constants;
import com.sum.shy.common.KeywordTable;
import com.sum.shy.element.Token;

public class StructRecognizerImpl implements StructRecognizer {

	@Override
	public String getSyntax(List<Token> tokens) {

		Token first = tokens.get(0);

		// keyword
		if (KeywordTable.isStruct(first.toString()))
			return first.toString();

		// end
		if (tokens.size() == 1 && "}".equals(first.toString()))
			return Constants.END_SYNTAX;

		// annotation
		if (tokens.size() == 1 && first.isAnnotation())
			return Constants.ANNOTATION_SYNTAX;

		// unknown
		return null;

	}

}
