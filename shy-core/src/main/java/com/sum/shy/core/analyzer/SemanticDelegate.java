package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

/**
 * 语义分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class SemanticDelegate {

	// 某些句式,token需要特殊处理
	public static final String[] SYNTAXS = new String[] { "package", "import", "def", "class", "func" };

	// 关键字
	public static final String[] KEYWORDS = new String[] { "package", "import", "def", "class", "func", "return", "if",
			"else", "for", "while", "do" };

	// 操作符
	public static final String[] OPERATORS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "=", "+", "-", "*", "/",
			"%", "<", ">" };
	// 分隔符
	public static final String[] SEPARATORS = new String[] { "[", "]", "{", "}", "(", ")", ":", "," };

	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");
	public static final Pattern INVOKE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_INIT_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_STATIC_PATTERN = Pattern
			.compile("^[A-Z]+[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_MEMBER_PATTERN = Pattern.compile("^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9\\.]+$");
	private static final Pattern VAR_MEMBER_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$");
	private static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");

	/**
	 * 语义分析
	 * 
	 * @param syntax
	 * 
	 * @param words
	 * @return
	 */
	public static List<Token> getTokens(String syntax, List<String> words) {

		List<Token> tokens = new ArrayList<>();

		if (isKeywordSyntax(syntax)) {// 有些句式需要特殊处理
			for (String word : words) {
				String type = getKeywordTokenType(word);
				Object value = word;
				tokens.add(new Token(type, value, null));
			}

		} else if (Constants.DECLARE_SYNTAX.equals(syntax)) {// 类型声明
			tokens.add(new Token(Constants.TYPE_TOKEN, words.get(0), null));
			tokens.add(new Token(Constants.VAR_TOKEN, words.get(1), null));

		} else {
			for (String word : words) {
				String type = getTokenType(word);
				Object value = getTokenValue(type, word);
				Map<String, String> attachments = getAttachments(word, type, value);
				tokens.add(new Token(type, value, attachments));
			}

		}

		return tokens;
	}

	private static String getKeywordTokenType(String word) {
		if (isKeyword(word)) {
			return Constants.KEYWORD_TOKEN;
		} else if (isSeparator(word)) {
			return Constants.SEPARATOR_TOKEN;
		} else {
			return Constants.KEYWORD_PARAM_TOKEN;
		}
	}

	public static String getTokenType(String word) {

		if (isKeyword(word)) {// 关键字
			return Constants.KEYWORD_TOKEN;
		} else if (isOperator(word)) {// 是否操作符
			return Constants.OPERATOR_TOKEN;
		} else if (isSeparator(word)) {// 是否分隔符
			return Constants.SEPARATOR_TOKEN;
		} else {// 是否一些值
			if (isNull(word)) {
				return Constants.NULL_TOKEN;
			} else if (isBoolean(word)) {
				return Constants.BOOLEAN_TOKEN;
			} else if (isInt(word)) {
				return Constants.INT_TOKEN;
			} else if (isDouble(word)) {
				return Constants.DOUBLE_TOKEN;
			} else if (isStr(word)) {
				return Constants.STR_TOKEN;
			} else if (isArray(word)) {
				return Constants.ARRAY_TOKEN;
			} else if (isMap(word)) {
				return Constants.MAP_TOKEN;
			} else if (isInvoke(word)) {
				return getInvokeTokenType(word);
			} else if (isVariable(word)) {
				return getVarTokenType(word);
			}
			return Constants.UNKNOWN;
		}

	}

	private static String getInvokeTokenType(String word) {

		if (INVOKE_INIT_PATTERN.matcher(word).matches()) {// 构造函数
			return Constants.INVOKE_INIT_TOKEN;
		}
		if (INVOKE_STATIC_PATTERN.matcher(word).matches()) {
			return Constants.INVOKE_STATIC_TOKEN;
		}
		if (INVOKE_MEMBER_PATTERN.matcher(word).matches()) {
			return Constants.INVOKE_MEMBER_TOKEN;
		}
		return Constants.UNKNOWN;
	}

	private static String getVarTokenType(String word) {

		if (VAR_MEMBER_PATTERN.matcher(word).matches()) {// 构造函数
			return Constants.MEMBER_VAR_TOKEN;
		}
		return Constants.VAR_TOKEN;
	}

	private static Object getTokenValue(String type, String word) {

		if (Constants.ARRAY_TOKEN.equals(type)) {// 如果是数组,则解析子语句
			String str = word.substring(1, word.length() - 1);
			List<String> words = LexicalAnalyzer.getWords(str);
			words.add(0, "[");
			words.add("]");
			// 获取tokens
			List<Token> tokens = getTokens(null, words);
			// 生成子语句
			return new Stmt(word, words, tokens);

		} else if (Constants.MAP_TOKEN.equals(type)) {
			String str = word.substring(1, word.length() - 1);
			List<String> words = LexicalAnalyzer.getWords(str);
			words.add(0, "{");
			words.add("}");
			// 获取tokens
			List<Token> tokens = getTokens(null, words);
			// 生成子语句
			return new Stmt(word, words, tokens);

		} else if (isInvokeTokenType(type)) {
			String prefix = word.substring(0, word.indexOf("("));
			String str = word.substring(word.indexOf("(") + 1, word.lastIndexOf(")"));
			List<String> words = LexicalAnalyzer.getWords(str);
			words.add(0, "(");
			words.add(")");
			// 获取tokens
			List<Token> tokens = getTokens(null, words);
			// 追加一个元素在头部
			tokens.add(0, new Token(Constants.PREFIX_TOKEN, prefix, null));
			// 生成子语句
			return new Stmt(word, words, tokens);
		}

		return word;
	}

	private static Map<String, String> getAttachments(String word, String type, Object value) {

		Map<String, String> attachments = new HashMap<>();
		if (Constants.INVOKE_INIT_TOKEN.equals(type)) {
			attachments.put(Constants.INIT_METHOD_NAME_ATTACHMENT, getInitMethodName(word));

		} else if (Constants.INVOKE_STATIC_TOKEN.equals(type)) {
			attachments.put(Constants.TYPE_ATTACHMENT, getTypeName(word));
			attachments.put(Constants.STATIC_METHOD_NAME_ATTACHMENT, getStaticMethodName(word));

		} else if (Constants.INVOKE_MEMBER_TOKEN.equals(type)) {
			attachments.put(Constants.VAR_NAME_ATTACHMENT, getVarName(word));
			attachments.put(Constants.MEMBER_METHOD_NAME_ATTACHMENT, getMemberMethodName(word));

		} else if (Constants.MEMBER_VAR_TOKEN.equals(type)) {
			attachments.put(Constants.VAR_NAME_ATTACHMENT, getVarName(word));
			attachments.put(Constants.MEMBER_VAR_NAME_ATTACHMENT, getMemberVarName(word));
		}

		return attachments;
	}

	private static boolean contain(String[] strs, String word) {
		for (String str : strs) {
			if (str.equals(word)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isKeywordSyntax(String syntax) {
		return contain(SYNTAXS, syntax);
	}

	private static boolean isInvokeTokenType(String type) {
		return Constants.INVOKE_INIT_TOKEN.equals(type) || Constants.INVOKE_STATIC_TOKEN.equals(type)
				|| Constants.INVOKE_MEMBER_TOKEN.equals(type);
	}

	private static boolean isKeyword(String word) {
		return contain(KEYWORDS, word);
	}

	public static boolean isOperator(String word) {
		return contain(OPERATORS, word);
	}

	public static boolean isSeparator(String word) {
		return contain(SEPARATORS, word);
	}

	private static boolean isNull(String word) {
		return "null".equals(word);
	}

	public static boolean isBoolean(String word) {
		return BOOLEAN_PATTERN.matcher(word).matches();
	}

	public static boolean isInt(String word) {
		return INT_PATTERN.matcher(word).matches();
	}

	public static boolean isDouble(String word) {
		return DOUBLE_PATTERN.matcher(word).matches();
	}

	public static boolean isStr(String word) {
		return STR_PATTERN.matcher(word).matches();
	}

	public static boolean isInvoke(String word) {
		return INVOKE_PATTERN.matcher(word).matches();
	}

	public static boolean isArray(String word) {
		return ARRAY_PATTERN.matcher(word).matches();
	}

	public static boolean isMap(String word) {
		return MAP_PATTERN.matcher(word).matches();
	}

	public static boolean isVariable(String word) {
		return VAR_PATTERN.matcher(word).matches();
	}

	public static boolean isType(String word) {
		return TYPE_PATTERN.matcher(word).matches();
	}

	public static String getInitMethodName(String word) {
		return word.substring(0, word.indexOf("("));
	}

	private static String getTypeName(String word) {
		return word.substring(0, word.indexOf("."));
	}

	private static String getVarName(String word) {
		return word.substring(0, word.indexOf("."));
	}

	private static String getStaticMethodName(String word) {
		return word.substring(word.indexOf(".") + 1, word.indexOf("("));
	}

	private static String getMemberMethodName(String word) {
		return word.substring(word.indexOf(".") + 1, word.indexOf("("));
	}

	private static String getMemberVarName(String word) {
		return word.substring(word.indexOf(".") + 1, word.length());
	}

}
