package com.sum.spirit.core.element.action;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.LiteralEnum;
import com.sum.spirit.common.enums.PrimitiveEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.element.entity.Token;

public abstract class AbstractSemanticParser implements SemanticParser {

	public static final Pattern PATH_PATTERN = Pattern.compile("^(\\w+\\.)+\\w+$");
	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");

	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*$");
	public static final Pattern TYPE_ARRAY_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]$");
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*<[\\s\\S]+>$");

	public static final Pattern PRIMITIVE_ARRAY_INIT_PATTERN = Pattern.compile("^(" + PrimitiveEnum.PRIMITIVE_ENUM + ")\\[\\d+\\]$");
	public static final Pattern PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN = Pattern.compile("^(" + PrimitiveEnum.PRIMITIVE_ENUM + ")\\[\\]\\{[\\s\\S]*\\}$");
	public static final Pattern TYPE_ARRAY_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\d+\\]$");
	public static final Pattern TYPE_ARRAY_CERTAIN_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]\\{[\\s\\S]*\\}$");
	public static final Pattern TYPE_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*(<[\\s\\S]+>)?\\([\\s\\S]*\\)$");

	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	public static final Pattern CONST_VAR_PATTERN = Pattern.compile("^[A-Z_]{2,}$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^[a-z]+\\w*$");
	public static final Pattern LOCAL_METHOD_PATTERN = Pattern.compile("^[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_FIELD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*$");
	public static final Pattern VISIT_METHOD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_INDEX_PATTERN = Pattern.compile("^\\[\\d+\\]$");

	public static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

	@Override
	public List<Token> getTokens(List<String> words, boolean insideType) {
		List<Token> tokens = new ArrayList<>();
		for (String word : words) {
			tokens.add(getToken(word, insideType));
		}
		return tokens;
	}

	@Override
	public boolean isPath(String word) {
		return !LiteralEnum.DOUBLE_PATTERN.matcher(word).matches() && PATH_PATTERN.matcher(word).matches();
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
		return !CONST_VAR_PATTERN.matcher(word).matches() && //
				(PrimitiveEnum.isPrimitiveBySimple(word) || //
						PrimitiveEnum.isPrimitiveArrayBySimple(word) || //
						TYPE_PATTERN.matcher(word).matches() || //
						TYPE_ARRAY_PATTERN.matcher(word).matches() || //
						GENERIC_TYPE_PATTERN.matcher(word).matches());
	}

	@Override
	public boolean isInit(String word) {
		return PRIMITIVE_ARRAY_INIT_PATTERN.matcher(word).matches() || //
				PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches() || //
				TYPE_ARRAY_INIT_PATTERN.matcher(word).matches() || //
				TYPE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches() || //
				TYPE_INIT_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isValue(String word) {
		return LiteralEnum.NULL_PATTERN.matcher(word).matches() || //
				LiteralEnum.BOOLEAN_PATTERN.matcher(word).matches() || //
				LiteralEnum.CHAR_PATTERN.matcher(word).matches() || //
				LiteralEnum.INT_PATTERN.matcher(word).matches() || //
				LiteralEnum.LONG_PATTERN.matcher(word).matches() || //
				LiteralEnum.DOUBLE_PATTERN.matcher(word).matches() || //
				LiteralEnum.STRING_PATTERN.matcher(word).matches() || //
				(!VISIT_INDEX_PATTERN.matcher(word).matches() && LiteralEnum.LIST_PATTERN.matcher(word).matches()) || // not be "[0]"
				LiteralEnum.MAP_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isSubexpress(String word) {
		return SUBEXPRESS_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isVariable(String word) {
		return VAR_PATTERN.matcher(word).matches() || CONST_VAR_PATTERN.matcher(word).matches();
	}

	@Override
	public boolean isAccess(String word) {
		return LOCAL_METHOD_PATTERN.matcher(word).matches() || //
				VISIT_FIELD_PATTERN.matcher(word).matches() || //
				VISIT_METHOD_PATTERN.matcher(word).matches() || //
				VISIT_INDEX_PATTERN.matcher(word).matches();
	}

	public TokenTypeEnum getInitTokenType(String word) {
		if (PRIMITIVE_ARRAY_INIT_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.ARRAY_INIT;
		}
		if (PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.ARRAY_INIT;
		}
		if (TYPE_ARRAY_INIT_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.ARRAY_INIT;
		}
		if (TYPE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.ARRAY_INIT;
		}
		if (TYPE_INIT_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.TYPE_INIT;
		}
		return null;
	}

	public TokenTypeEnum getValueTokenType(String word) {
		if (LiteralEnum.NULL_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.NULL;
		}
		if (LiteralEnum.BOOLEAN_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.BOOL;
		}
		if (LiteralEnum.CHAR_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.CHAR;
		}
		if (LiteralEnum.INT_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.INT;
		}
		if (LiteralEnum.LONG_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.LONG;
		}
		if (LiteralEnum.DOUBLE_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.DOUBLE;
		}
		if (LiteralEnum.STRING_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.STRING;
		}
		if (LiteralEnum.LIST_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.LIST;
		}
		if (LiteralEnum.MAP_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.MAP;
		}
		return null;
	}

	public TokenTypeEnum getSubexpressTokenType(String word) {
		if (isType(getCastType(word))) {
			return TokenTypeEnum.CAST;
		}
		return TokenTypeEnum.SUBEXPRESS;
	}

	public TokenTypeEnum getAccessTokenType(String word) {
		if (LOCAL_METHOD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.LOCAL_METHOD;
		}
		if (VISIT_FIELD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_FIELD;
		}
		if (VISIT_METHOD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_METHOD;
		}
		if (VISIT_INDEX_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_INDEX;
		}
		return null;
	}

	public String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

}
