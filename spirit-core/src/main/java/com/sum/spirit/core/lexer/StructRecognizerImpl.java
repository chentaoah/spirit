package com.sum.spirit.core.lexer;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.api.lexer.StructRecognizer;
import com.sum.spirit.pojo.common.KeywordTable;
import com.sum.spirit.pojo.common.SyntaxEnum;
import com.sum.spirit.pojo.element.Token;

@Component
public class StructRecognizerImpl implements StructRecognizer {

	@Override
	public SyntaxEnum getSyntax(List<Token> tokens) {

		Token first = tokens.get(0);

		// keyword
		if (KeywordTable.isStruct(first.toString()))
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
