package com.sum.spirit.api.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.KeywordEnum;
import com.sum.spirit.pojo.common.SymbolTable;
import com.sum.spirit.pojo.element.Token;

public interface SemanticParser {

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
	public static final Pattern CONST_PATTERN = Pattern.compile("^_[A-Z_]*_$");
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

	default List<Token> getTokens(List<String> words) {
		List<Token> tokens = new ArrayList<>();
		for (String word : words)
			tokens.add(getToken(word));
		return tokens;
	}

	default boolean isPath(String word) {
		return !DOUBLE_PATTERN.matcher(word).matches() && PATH_PATTERN.matcher(word).matches();
	}

	default boolean isAnnotation(String word) {
		return ANNOTATION_PATTERN.matcher(word).matches();
	}

	default boolean isKeyword(String word) {
		return KeywordEnum.isKeyword(word);
	}

	default boolean isOperator(String word) {
		return SymbolTable.isOperator(word);
	}

	default boolean isSeparator(String word) {
		return SymbolTable.isSeparator(word);
	}

	default boolean isType(String word) {
		return PRIMITIVE_PATTERN.matcher(word).matches() || PRIMITIVE_ARRAY_PATTERN.matcher(word).matches() || TYPE_PATTERN.matcher(word).matches()
				|| TYPE_ARRAY_PATTERN.matcher(word).matches() || GENERIC_TYPE_PATTERN.matcher(word).matches();
	}

	default boolean isInit(String word) {
		return PRIMITIVE_ARRAY_INIT_PATTERN.matcher(word).matches() || PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches()
				|| TYPE_ARRAY_INIT_PATTERN.matcher(word).matches() || TYPE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches()
				|| TYPE_INIT_PATTERN.matcher(word).matches();
	}

	default String getInitTokenType(String word) {
		if (PRIMITIVE_ARRAY_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (TYPE_ARRAY_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (TYPE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (TYPE_INIT_PATTERN.matcher(word).matches())
			return Constants.TYPE_INIT_TOKEN;
		return null;
	}

	default boolean isValue(String word) {
		return NULL_PATTERN.matcher(word).matches() || BOOL_PATTERN.matcher(word).matches() || CHAR_PATTERN.matcher(word).matches()
				|| INT_PATTERN.matcher(word).matches() || LONG_PATTERN.matcher(word).matches() || DOUBLE_PATTERN.matcher(word).matches()
				|| STR_PATTERN.matcher(word).matches() || LIST_PATTERN.matcher(word).matches() || MAP_PATTERN.matcher(word).matches();
	}

	default String getValueTokenType(String word) {
		if (NULL_PATTERN.matcher(word).matches())
			return Constants.NULL_TOKEN;
		if (BOOL_PATTERN.matcher(word).matches())
			return Constants.BOOL_TOKEN;
		if (CHAR_PATTERN.matcher(word).matches())
			return Constants.CHAR_TOKEN;
		if (INT_PATTERN.matcher(word).matches())
			return Constants.INT_TOKEN;
		if (LONG_PATTERN.matcher(word).matches())
			return Constants.LONG_TOKEN;
		if (DOUBLE_PATTERN.matcher(word).matches())
			return Constants.DOUBLE_TOKEN;
		if (STR_PATTERN.matcher(word).matches())
			return Constants.STR_TOKEN;
		if (LIST_PATTERN.matcher(word).matches())
			return Constants.LIST_TOKEN;
		if (MAP_PATTERN.matcher(word).matches())
			return Constants.MAP_TOKEN;
		return null;
	}

	default boolean isSubexpress(String word) {
		return SUBEXPRESS_PATTERN.matcher(word).matches();
	}

	default String getSubexpressTokenType(String word) {
		if (isType(getCastType(word)))
			return Constants.CAST_TOKEN;
		return Constants.SUBEXPRESS_TOKEN;
	}

	default boolean isVar(String word) {
		return VAR_PATTERN.matcher(word).matches() || CONST_PATTERN.matcher(word).matches();
	}

	default boolean isAccess(String word) {
		return INVOKE_LOCAL_PATTERN.matcher(word).matches() || VISIT_FIELD_PATTERN.matcher(word).matches() || INVOKE_METHOD_PATTERN.matcher(word).matches()
				|| VISIT_ARRAY_INDEX_PATTERN.matcher(word).matches() || ARRAY_INDEX_PATTERN.matcher(word).matches();
	}

	default String getAccessTokenType(String word) {
		if (INVOKE_LOCAL_PATTERN.matcher(word).matches())
			return Constants.LOCAL_METHOD_TOKEN;
		if (VISIT_FIELD_PATTERN.matcher(word).matches())
			return Constants.VISIT_FIELD_TOKEN;
		if (INVOKE_METHOD_PATTERN.matcher(word).matches())
			return Constants.INVOKE_METHOD_TOKEN;
		if (VISIT_ARRAY_INDEX_PATTERN.matcher(word).matches())
			return Constants.VISIT_ARRAY_INDEX_TOKEN;
		if (ARRAY_INDEX_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INDEX_TOKEN;
		return null;
	}

	default String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

	Token getToken(String word);

}
