package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.api.lexer.StructRecognizer;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.SyntaxEnum;

@Component
public class StructRecognizerImpl implements StructRecognizer {

	@Override
	public SyntaxEnum getSyntax(List<Token> tokens) {

		Token first = tokens.get(0);

		// keyword
		if (KeywordEnum.isStruct(first.toString()))
			return SyntaxEnum.valueOf(first.toString().toUpperCase());

		// end
		if (tokens.size() == 1 && "}".equals(first.toString()))
			return SyntaxEnum.END;

		// annotation
		if (tokens.size() == 1 && first.isAnnotation())
			return SyntaxEnum.ANNOTATION;

		// unknown
		return null;

	}

}
