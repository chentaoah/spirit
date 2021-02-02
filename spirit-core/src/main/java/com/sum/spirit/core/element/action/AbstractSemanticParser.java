package com.sum.spirit.core.element.action;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sum.spirit.core.common.enums.KeywordEnum;
import com.sum.spirit.core.common.enums.SymbolEnum;
import com.sum.spirit.core.common.enums.TokenTypeEnum;
import com.sum.spirit.core.element.entity.Token;

public abstract class AbstractSemanticParser {

	public static final Pattern PATH_PATTERN = Pattern.compile("^(\\w+\\.)+\\w+$");
	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");

	public static final String PRIMITIVE_ENUM = "void|boolean|char|short|int|long|float|double|byte";

	public static final Pattern PRIMITIVE_PATTERN = Pattern.compile("^(" + PRIMITIVE_ENUM + ")$");
	public static final Pattern PRIMITIVE_ARRAY_PATTERN = Pattern.compile("^(" + PRIMITIVE_ENUM + ")\\[\\]$");
	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*$");
	public static final Pattern TYPE_ARRAY_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]$");
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*<[\\s\\S]+>$");

	public static final Pattern PRIMITIVE_ARRAY_INIT_PATTERN = Pattern.compile("^(" + PRIMITIVE_ENUM + ")\\[\\d+\\]$");
	public static final Pattern PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN = Pattern.compile("^(" + PRIMITIVE_ENUM + ")\\[\\]\\{[\\s\\S]*\\}$");
	public static final Pattern TYPE_ARRAY_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\d+\\]$");
	public static final Pattern TYPE_ARRAY_CERTAIN_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]\\{[\\s\\S]*\\}$");
	public static final Pattern TYPE_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w*(<[\\s\\S]+>)?\\([\\s\\S]*\\)$");

	public static final Pattern NULL_PATTERN = Pattern.compile("^null$");
	public static final Pattern BOOL_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern CHAR_PATTERN = Pattern.compile("^'[\\s\\S]*'$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern LONG_PATTERN = Pattern.compile("^\\d+L$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern LIST_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");

	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^[a-z]+\\w*$");
	public static final Pattern CONST_VAR_PATTERN = Pattern.compile("^[A-Z_]{2,}$");
	public static final Pattern INVOKE_LOCAL_PATTERN = Pattern.compile("^[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_FIELD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*$");
	public static final Pattern INVOKE_METHOD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_ARRAY_INDEX_PATTERN = Pattern.compile("^\\.[a-z]+\\w*\\[\\d+\\]$");
	public static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("^[a-z]+\\w*\\[\\d+\\]$");

	public static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

	public static boolean isPrimitive(String word) {
		return PRIMITIVE_PATTERN.matcher(word).matches();
	}

	public static boolean isDouble(String word) {
		return DOUBLE_PATTERN.matcher(word).matches();
	}

	public List<Token> getTokens(List<String> words) {
		return getTokens(words, false);
	}

	public List<Token> getTokens(List<String> words, boolean insideType) {
		List<Token> tokens = new ArrayList<>();
		for (String word : words) {
			tokens.add(getToken(word, insideType));
		}
		return tokens;
	}

	public boolean isPath(String word) {
		return !DOUBLE_PATTERN.matcher(word).matches() && PATH_PATTERN.matcher(word).matches();
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

	public boolean isType(String word) {
		return !CONST_VAR_PATTERN.matcher(word).matches() && //
				(PRIMITIVE_PATTERN.matcher(word).matches() || //
						PRIMITIVE_ARRAY_PATTERN.matcher(word).matches() || //
						TYPE_PATTERN.matcher(word).matches() || //
						TYPE_ARRAY_PATTERN.matcher(word).matches() || //
						GENERIC_TYPE_PATTERN.matcher(word).matches());
	}

	public boolean isInit(String word) {
		return PRIMITIVE_ARRAY_INIT_PATTERN.matcher(word).matches() || //
				PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches() || //
				TYPE_ARRAY_INIT_PATTERN.matcher(word).matches() || //
				TYPE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches() || //
				TYPE_INIT_PATTERN.matcher(word).matches();
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

	public boolean isValue(String word) {
		return NULL_PATTERN.matcher(word).matches() || //
				BOOL_PATTERN.matcher(word).matches() || //
				CHAR_PATTERN.matcher(word).matches() || //
				INT_PATTERN.matcher(word).matches() || //
				LONG_PATTERN.matcher(word).matches() || //
				DOUBLE_PATTERN.matcher(word).matches() || //
				STR_PATTERN.matcher(word).matches() || //
				LIST_PATTERN.matcher(word).matches() || //
				MAP_PATTERN.matcher(word).matches();
	}

	public TokenTypeEnum getValueTokenType(String word) {
		if (NULL_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.NULL;
		}
		if (BOOL_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.BOOL;
		}
		if (CHAR_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.CHAR;
		}
		if (INT_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.INT;
		}
		if (LONG_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.LONG;
		}
		if (DOUBLE_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.DOUBLE;
		}
		if (STR_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.STRING;
		}
		if (LIST_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.LIST;
		}
		if (MAP_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.MAP;
		}
		return null;
	}

	public boolean isSubexpress(String word) {
		return SUBEXPRESS_PATTERN.matcher(word).matches();
	}

	public TokenTypeEnum getSubexpressTokenType(String word) {
		if (isType(getCastType(word))) {
			return TokenTypeEnum.CAST;
		}
		return TokenTypeEnum.SUBEXPRESS;
	}

	public boolean isVariable(String word) {
		return VAR_PATTERN.matcher(word).matches() || CONST_VAR_PATTERN.matcher(word).matches();
	}

	public boolean isAccess(String word) {
		return INVOKE_LOCAL_PATTERN.matcher(word).matches() || //
				VISIT_FIELD_PATTERN.matcher(word).matches() || //
				INVOKE_METHOD_PATTERN.matcher(word).matches() || //
				VISIT_ARRAY_INDEX_PATTERN.matcher(word).matches() || //
				ARRAY_INDEX_PATTERN.matcher(word).matches();
	}

	public TokenTypeEnum getAccessTokenType(String word) {
		if (INVOKE_LOCAL_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.LOCAL_METHOD;
		}
		if (VISIT_FIELD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_FIELD;
		}
		if (INVOKE_METHOD_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.INVOKE_METHOD;
		}
		if (VISIT_ARRAY_INDEX_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.VISIT_ARRAY_INDEX;
		}
		if (ARRAY_INDEX_PATTERN.matcher(word).matches()) {
			return TokenTypeEnum.ARRAY_INDEX;
		}
		return null;
	}

	public String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

	public abstract Token getToken(String word, boolean insideType);

}
