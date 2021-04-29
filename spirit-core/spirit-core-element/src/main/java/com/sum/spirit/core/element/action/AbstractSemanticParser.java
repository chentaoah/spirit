package com.sum.spirit.core.element.action;

import java.util.List;
import java.util.regex.Pattern;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.LiteralEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.enums.TypeEnum;
import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.element.entity.Token;

public abstract class AbstractSemanticParser implements SemanticParser {

	public static final Pattern PATH_PATTERN = Pattern.compile("^(\\w+\\.)+\\w+$");
	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");

	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	public static final Pattern CONST_VAR_PATTERN = Pattern.compile("^[A-Z_]{2,}$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^[a-z]+\\w*$");
	public static final Pattern LOCAL_METHOD_PATTERN = Pattern.compile("^[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_FIELD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*$");
	public static final Pattern VISIT_METHOD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_INDEX_PATTERN = Pattern.compile("^\\[\\d+\\]$");

	public static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

	@Override
	public List<Token> getTokens(List<String> words) {
		return ListUtils.collectAll(words, word -> true, word -> getToken(word));
	}

	@Override
	public List<Token> getTokensInsideType(List<String> words) {
		return ListUtils.collectAll(words, word -> true, word -> getTokenInsideType(word));
	}

	@Override
	public Token getToken(String word) {
		return getToken(word, false);
	}

	@Override
	public Token getTokenInsideType(String word) {
		return getToken(word, true);
	}

	@Override
	public boolean isPath(String word) {
		return !LiteralEnum.isDouble(word) && PATH_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isAnnotation(String word) {
		return ANNOTATION_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isKeyword(String word) {
		return KeywordEnum.isKeyword(word);
	}

	@Override
	public boolean isOperator(String word) {
		return SymbolEnum.getOperator(word) != null;
	}

	@Override
	public boolean isSeparator(String word) {
		return SymbolEnum.getSeparator(word) != null;
	}

	@Override
	public boolean isType(String word) {
		return TypeEnum.isAnyType(word);
	}

	@Override
	public boolean isInit(String word) {
		return TypeEnum.isAnyInit(word);
	}

	@Override
	public boolean isLiteral(String word) {
		return LiteralEnum.isNull(word) || LiteralEnum.isBoolean(word) || LiteralEnum.isChar(word) || //
				LiteralEnum.isInt(word) || LiteralEnum.isLong(word) || LiteralEnum.isDouble(word) || //
				LiteralEnum.isString(word) || LiteralEnum.isList(word) || LiteralEnum.isMap(word);
	}

	@Override
	public boolean isSubexpress(String word) {
		return SUBEXPRESS_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isVariable(String word) {
		return CONST_VAR_PATTERN.matcher(word).matches() || VAR_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isAccess(String word) {
		return LOCAL_METHOD_PATTERN.matcher(word).matches() || //
				VISIT_FIELD_PATTERN.matcher(word).matches() || //
				VISIT_METHOD_PATTERN.matcher(word).matches() || //
				VISIT_INDEX_PATTERN.matcher(word).matches();
	}

	public TokenTypeEnum getInitTokenType(String word) {
		if (TypeEnum.isPrimitiveArraySizeInit(word) || TypeEnum.isPrimitiveArrayLiteralInit(word)) {
			return TokenTypeEnum.ARRAY_INIT;

		} else if (TypeEnum.isTypeArraySizeInit(word) || TypeEnum.isTypeArrayLiteralInit(word)) {
			return TokenTypeEnum.ARRAY_INIT;

		} else if (TypeEnum.isTypeInit(word)) {
			return TokenTypeEnum.TYPE_INIT;
		}
		return null;
	}

	public TokenTypeEnum getLiteralTokenType(String word) {
		if (LiteralEnum.isNull(word)) {
			return TokenTypeEnum.NULL;

		} else if (LiteralEnum.isBoolean(word)) {
			return TokenTypeEnum.BOOLEAN;

		} else if (LiteralEnum.isChar(word)) {
			return TokenTypeEnum.CHAR;

		} else if (LiteralEnum.isInt(word)) {
			return TokenTypeEnum.INT;

		} else if (LiteralEnum.isLong(word)) {
			return TokenTypeEnum.LONG;

		} else if (LiteralEnum.isDouble(word)) {
			return TokenTypeEnum.DOUBLE;

		} else if (LiteralEnum.isString(word)) {
			return TokenTypeEnum.STRING;

		} else if (LiteralEnum.isList(word)) {
			return TokenTypeEnum.LIST;

		} else if (LiteralEnum.isMap(word)) {
			return TokenTypeEnum.MAP;
		}
		return null;
	}

	public TokenTypeEnum getSubexpressTokenType(String word) {
		if (isType(getCastType(word))) {
			return TokenTypeEnum.CAST;
		} else {
			return TokenTypeEnum.SUBEXPRESS;
		}
	}

	public TokenTypeEnum getAccessTokenType(String word) {
		if (LOCAL_METHOD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.LOCAL_METHOD;

		} else if (VISIT_FIELD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_FIELD;

		} else if (VISIT_METHOD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_METHOD;

		} else if (VISIT_INDEX_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_INDEX;
		}
		return null;
	}

	public String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

	public abstract Token getToken(String word, boolean insideType);

}
