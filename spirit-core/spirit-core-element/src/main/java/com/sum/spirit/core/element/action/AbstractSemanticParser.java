package com.sum.spirit.core.element.action;

import java.util.List;
import java.util.regex.Pattern;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.pattern.LiteralPattern;
import com.sum.spirit.common.pattern.TypePattern;
import com.sum.spirit.common.pattern.VisitPattern;
import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.element.entity.SemanticContext;
import com.sum.spirit.core.element.entity.Token;

public abstract class AbstractSemanticParser implements SemanticParser {

	public static final Pattern PATH_PATTERN = Pattern.compile("^(\\w+\\.)+\\w+$");
	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");
	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^[a-z]+\\w*$");
	public static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

	@Override
	public List<Token> getTokens(SemanticContext context, List<String> words) {
		return ListUtils.collectAll(words, word -> true, word -> getToken(context, word));
	}

	public boolean isPath(String word) {
		return !LiteralPattern.isDouble(word) && PATH_PATTERN.matcher(word).matches();
	}

	public boolean isAnnotation(String word) {
		return ANNOTATION_PATTERN.matcher(word).matches();
	}

	public boolean isKeyword(String word) {
		return KeywordEnum.isKeyword(word);
	}

	public boolean isOperator(String word) {
		return SymbolEnum.getOperator(word) != null;
	}

	public boolean isSeparator(String word) {
		return SymbolEnum.getSeparator(word) != null;
	}

	@Override
	public boolean isType(String word) {
		return TypePattern.isAnyType(word);
	}

	public boolean isVariable(String word) {
		return LiteralPattern.isConstVariable(word) || VAR_PATTERN.matcher(word).matches();
	}

	public TokenTypeEnum getInitTokenType(String word) {
		if (TypePattern.isPrimitiveArraySizeInit(word) || TypePattern.isPrimitiveArrayLiteralInit(word)) {
			return TokenTypeEnum.ARRAY_INIT;

		} else if (TypePattern.isTypeArraySizeInit(word) || TypePattern.isTypeArrayLiteralInit(word)) {
			return TokenTypeEnum.ARRAY_INIT;

		} else if (TypePattern.isTypeInit(word)) {
			return TokenTypeEnum.TYPE_INIT;
		}
		return null;
	}

	public TokenTypeEnum getLiteralTokenType(String word) {
		if (LiteralPattern.isNull(word)) {
			return TokenTypeEnum.NULL;

		} else if (LiteralPattern.isBoolean(word)) {
			return TokenTypeEnum.BOOLEAN;

		} else if (LiteralPattern.isChar(word)) {
			return TokenTypeEnum.CHAR;

		} else if (LiteralPattern.isInt(word)) {
			return TokenTypeEnum.INT;

		} else if (LiteralPattern.isLong(word)) {
			return TokenTypeEnum.LONG;

		} else if (LiteralPattern.isDouble(word)) {
			return TokenTypeEnum.DOUBLE;

		} else if (LiteralPattern.isString(word)) {
			return TokenTypeEnum.STRING;

		} else if (LiteralPattern.isList(word)) {
			return TokenTypeEnum.LIST;

		} else if (LiteralPattern.isMap(word)) {
			return TokenTypeEnum.MAP;
		}
		return null;
	}

	public TokenTypeEnum getSubexpressTokenType(String word) {
		if (SUBEXPRESS_PATTERN.matcher(word).matches()) {
			if (isType(getCastType(word))) {
				return TokenTypeEnum.CAST;
			} else {
				return TokenTypeEnum.SUBEXPRESS;
			}
		}
		return null;
	}

	public TokenTypeEnum getAccessTokenType(String word) {
		if (VisitPattern.isLocalMethod(word)) {
			return TokenTypeEnum.LOCAL_METHOD;

		} else if (VisitPattern.isVisitField(word)) {
			return TokenTypeEnum.VISIT_FIELD;

		} else if (VisitPattern.isVisitMethod(word)) {
			return TokenTypeEnum.VISIT_METHOD;

		} else if (VisitPattern.isVisitIndex(word)) {
			return TokenTypeEnum.VISIT_INDEX;
		}
		return null;
	}

	public String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

}
