package com.sum.spirit.core.element.action;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.pattern.LiteralPattern;
import com.sum.spirit.common.pattern.TypePattern;
import com.sum.spirit.common.pattern.AccessPattern;
import com.sum.spirit.common.pattern.CommonPattern;
import com.sum.spirit.core.api.SemanticParser;

public abstract class AbstractSemanticParser implements SemanticParser {

	public boolean isAccessPath(String word) {
		return !LiteralPattern.isDouble(word) && AccessPattern.isAccessPath(word);
	}

	public boolean isAnnotation(String word) {
		return CommonPattern.isAnnotation(word);
	}

	public boolean isKeyword(String word) {
		return KeywordEnum.isKeyword(word);
	}

	public boolean isOperator(String word) {
		return SymbolEnum.isOperator(word);
	}

	public boolean isSeparator(String word) {
		return SymbolEnum.isSeparator(word);
	}

	@Override
	public boolean isType(String word) {
		return TypePattern.isAnyType(word);
	}

	public boolean isVariable(String word) {
		return LiteralPattern.isConstVariable(word) || CommonPattern.isVariable(word);
	}

}
