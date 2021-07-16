package com.gitee.spirit.core.element.action;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.common.enums.OperatorEnum;
import com.gitee.spirit.common.enums.SeparatorEnum;
import com.gitee.spirit.common.pattern.AccessPattern;
import com.gitee.spirit.common.pattern.CommonPattern;
import com.gitee.spirit.common.pattern.LiteralPattern;
import com.gitee.spirit.common.pattern.TypePattern;
import com.gitee.spirit.core.api.SemanticParser;

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
		return OperatorEnum.isOperator(word);
	}

	public boolean isSeparator(String word) {
		return SeparatorEnum.isSeparator(word);
	}

	@Override
	public boolean isType(String word) {
		return TypePattern.isAnyType(word);
	}

	public boolean isVariable(String word) {
		return LiteralPattern.isConstVariable(word) || CommonPattern.isVariable(word);
	}

}
