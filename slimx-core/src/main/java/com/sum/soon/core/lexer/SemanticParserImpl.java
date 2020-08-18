package com.sum.soon.core.lexer;

import java.util.List;
import java.util.regex.Pattern;

import com.sum.pisces.core.ProxyFactory;
import com.sum.soon.api.lexer.Lexer;
import com.sum.soon.api.lexer.SemanticParser;
import com.sum.soon.lib.Assert;
import com.sum.soon.pojo.common.Constants;
import com.sum.soon.pojo.common.KeywordTable;
import com.sum.soon.pojo.common.SymbolTable;
import com.sum.soon.pojo.element.Statement;
import com.sum.soon.pojo.element.Token;

public class SemanticParserImpl implements SemanticParser {
	// special
	public static final Pattern PATH_PATTERN = Pattern.compile("^(\\w+\\.)+\\w+$");
	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");
	// declaration of type
	public static final String PRIMITIVE_ENUM = "void|boolean|char|short|int|long|float|double|byte";
	public static final Pattern PRIMITIVE_PATTERN = Pattern.compile("^(" + PRIMITIVE_ENUM + ")$");
	public static final Pattern PRIMITIVE_ARRAY_PATTERN = Pattern.compile("^(" + PRIMITIVE_ENUM + ")\\[\\]$");
	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*$");
	public static final Pattern TYPE_ARRAY_PATTERN = Pattern.compile("^[A-Z]+\\w*\\[\\]$");
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w*<[\\s\\S]+>$");
	// base type and literal constant
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
	// expression
	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^[a-z]+\\w*$");
	public static final Pattern INVOKE_LOCAL_PATTERN = Pattern.compile("^[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_FIELD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*$");
	public static final Pattern INVOKE_METHOD_PATTERN = Pattern.compile("^\\.[a-z]+\\w*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_ARRAY_INDEX_PATTERN = Pattern.compile("^\\.[a-z]+\\w*\\[\\d+\\]$");
	public static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("^[a-z]+\\w*\\[\\d+\\]$");
	// prefix pattern
	public static final Pattern PREFIX_PATTERN = Pattern.compile("^(\\.)?\\w+$");

	public static Lexer lexer = ProxyFactory.get(Lexer.class);

	@Override
	public Token getToken(String word) {

		Token token = new Token();

		// 1.get token type
		getTokenType(word, token);

		// 2.get token value
		getTokenValue(word, token);

		// 3.get token Attachments
		getAttachments(word, token);

		return token;

	}

	public void getTokenType(String word, Token token) {

		if (isPath(word)) {
			token.type = Constants.PATH_TOKEN;

		} else if (isAnnotation(word)) {
			token.type = Constants.ANNOTATION_TOKEN;

		} else if (isKeyword(word)) {
			token.type = Constants.KEYWORD_TOKEN;

		} else if (isOperator(word)) {
			token.type = Constants.OPERATOR_TOKEN;

		} else if (isSeparator(word)) {
			token.type = Constants.SEPARATOR_TOKEN;

		} else if (isType(word)) {
			token.type = Constants.TYPE_TOKEN;

		} else if (isInit(word)) {
			token.type = getInitTokenType(word);

		} else if (isValue(word)) {
			token.type = getValueTokenType(word);

		} else if (isSubexpress(word)) {
			token.type = getSubexpressTokenType(word);

		} else if (isVar(word)) {
			token.type = Constants.VAR_TOKEN;

		} else if (isAccess(word)) {
			token.type = getAccessTokenType(word);
		}

		Assert.notNull(token.type, "Token type cannot be null!");
	}

	public static boolean isPath(String word) {
		return !DOUBLE_PATTERN.matcher(word).matches() && PATH_PATTERN.matcher(word).matches();
	}

	public static boolean isAnnotation(String word) {
		return ANNOTATION_PATTERN.matcher(word).matches();
	}

	public static boolean isKeyword(String word) {
		return KeywordTable.isKeyword(word);
	}

	public static boolean isOperator(String word) {
		return SymbolTable.isOperator(word);
	}

	public static boolean isSeparator(String word) {
		return SymbolTable.isSeparator(word);
	}

	public static boolean isType(String word) {
		return PRIMITIVE_PATTERN.matcher(word).matches() || PRIMITIVE_ARRAY_PATTERN.matcher(word).matches() || TYPE_PATTERN.matcher(word).matches()
				|| TYPE_ARRAY_PATTERN.matcher(word).matches() || GENERIC_TYPE_PATTERN.matcher(word).matches();
	}

	public static boolean isInit(String word) {
		return PRIMITIVE_ARRAY_INIT_PATTERN.matcher(word).matches() || PRIMITIVE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches()
				|| TYPE_ARRAY_INIT_PATTERN.matcher(word).matches() || TYPE_ARRAY_CERTAIN_INIT_PATTERN.matcher(word).matches()
				|| TYPE_INIT_PATTERN.matcher(word).matches();
	}

	public static String getInitTokenType(String word) {
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

	public static boolean isValue(String word) {
		return NULL_PATTERN.matcher(word).matches() || BOOL_PATTERN.matcher(word).matches() || CHAR_PATTERN.matcher(word).matches()
				|| INT_PATTERN.matcher(word).matches() || LONG_PATTERN.matcher(word).matches() || DOUBLE_PATTERN.matcher(word).matches()
				|| STR_PATTERN.matcher(word).matches() || LIST_PATTERN.matcher(word).matches() || MAP_PATTERN.matcher(word).matches();
	}

	public static String getValueTokenType(String word) {
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

	public static boolean isSubexpress(String word) {
		return SUBEXPRESS_PATTERN.matcher(word).matches();
	}

	public static String getSubexpressTokenType(String word) {
		if (isType(getCastType(word)))
			return Constants.CAST_TOKEN;
		return Constants.SUBEXPRESS_TOKEN;
	}

	public static boolean isVar(String word) {
		return VAR_PATTERN.matcher(word).matches();
	}

	public static boolean isAccess(String word) {
		return INVOKE_LOCAL_PATTERN.matcher(word).matches() || VISIT_FIELD_PATTERN.matcher(word).matches() || INVOKE_METHOD_PATTERN.matcher(word).matches()
				|| VISIT_ARRAY_INDEX_PATTERN.matcher(word).matches() || ARRAY_INDEX_PATTERN.matcher(word).matches();
	}

	public static String getAccessTokenType(String word) {
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

	public static boolean isDouble(String word) {
		return DOUBLE_PATTERN.matcher(word).matches();
	}

	public void getTokenValue(String word, Token token) {

		if (token.isType()) {
			token.value = getStatement(word, true);

		} else if (token.isArrayInit() || token.isList() || token.isMap() || token.isSubexpress() || token.isInvoke()) {
			token.value = getStatement(word, false);// split array init is to better add the keyword 'new'

		} else {
			token.value = word;
		}

	}

	public Object getStatement(String word, boolean isType) {

		if (isType && (!word.contains("<") && !word.contains(">")))
			return word;

		List<String> words = isType ? lexer.getWords(word, '<') : lexer.getWords(word, '[', '{', '(');
		List<Token> tokens = null;
		String first = words.get(0);
		if (PREFIX_PATTERN.matcher(first).matches()) {
			tokens = getTokens(words.subList(1, words.size()));
			tokens.add(0, new Token(Constants.PREFIX_TOKEN, first));
		} else {
			tokens = getTokens(words);
		}

		if (isType) {
			for (int i = 0; i < tokens.size(); i++) {
				Token token = tokens.get(i);
				if ("?".equals(token.toString())) {
					tokens.set(i, new Token(Constants.TYPE_TOKEN, "?"));

				} else if ("<".equals(token.toString())) {
					tokens.set(i, new Token(Constants.SEPARATOR_TOKEN, "<"));

				} else if (">".equals(token.toString())) {
					tokens.set(i, new Token(Constants.SEPARATOR_TOKEN, ">"));
				}
			}
		}

		Assert.notNull(tokens, "Tokens can not be null!");
		return new Statement(tokens);

	}

	public static void getAttachments(String word, Token token) {

		if (token.isArrayInit()) {
			token.setSimpleName(getPrefix(word) + "[]");

		} else if (token.isTypeInit()) {
			token.setSimpleName(getPrefix(word));

		} else if (token.isCast()) {
			token.setSimpleName(getCastType(word));

		} else if (token.isAccess()) {
			token.setMemberName(getPrefix(word));
		}

	}

	public static String getPrefix(String word) {
		int start = word.startsWith(".") ? 1 : 0;
		int end = word.length();
		if (word.contains("[")) {
			int index = word.indexOf("[");
			end = index < end ? index : end;
		}
		if (word.contains("(")) {
			int index = word.indexOf("(");
			end = index < end ? index : end;
		}
		return word.substring(start, end);
	}

	public static String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

}
