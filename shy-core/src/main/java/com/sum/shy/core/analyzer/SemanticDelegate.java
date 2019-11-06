package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Type;

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
			"%", "<", ">", "!" };
	// 分隔符
	public static final String[] SEPARATORS = new String[] { "[", "]", "{", "}", "(", ")", ":", "," };

	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");
	public static final Pattern INVOKE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.]*\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_INIT_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_STATIC_PATTERN = Pattern
			.compile("^[A-Z]+[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_MEMBER_PATTERN = Pattern.compile("^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_LOCAL_PATTERN = Pattern.compile("^[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_FLUENT_PATTERN = Pattern.compile("^\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern CAST_PATTERN = Pattern.compile("^\\([A-Z]+[a-zA-Z0-9]+\\)$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9\\.]+$");
	private static final Pattern MEMBER_VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$");
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
				Token token = new Token();
				getKeywordTokenType(token, word);
				token.value = word;
				tokens.add(token);
			}

		} else if (Constants.DECLARE_SYNTAX.equals(syntax)) {// 类型声明
			tokens.add(new Token(Constants.TYPE_TOKEN, words.get(0), null));
			tokens.add(new Token(Constants.VAR_TOKEN, words.get(1), null));

		} else {
			for (String word : words) {
				Token token = new Token();
				getTokenType(token, word);
				getTokenValue(token, word);
				getAttachments(token, word);
				tokens.add(token);
			}

		}

		return tokens;
	}

	private static void getKeywordTokenType(Token token, String word) {
		if (isKeyword(word)) {
			token.type = Constants.KEYWORD_TOKEN;
			return;
		} else if (isSeparator(word)) {
			token.type = Constants.SEPARATOR_TOKEN;
			return;
		} else {
			token.type = Constants.KEYWORD_PARAM_TOKEN;
			return;
		}
	}

	public static void getTokenType(Token token, String word) {

		if (isKeyword(word)) {// 关键字
			token.type = Constants.KEYWORD_TOKEN;
			return;
		} else if (isOperator(word)) {// 是否操作符
			token.type = Constants.OPERATOR_TOKEN;
			return;
		} else if (isSeparator(word)) {// 是否分隔符
			token.type = Constants.SEPARATOR_TOKEN;
			return;
		} else {// 是否一些值
			if (isNull(word)) {
				token.type = Constants.NULL_TOKEN;
				return;
			} else if (isBoolean(word)) {
				token.type = Constants.BOOLEAN_TOKEN;
				return;
			} else if (isInt(word)) {
				token.type = Constants.INT_TOKEN;
				return;
			} else if (isDouble(word)) {
				token.type = Constants.DOUBLE_TOKEN;
				return;
			} else if (isStr(word)) {
				token.type = Constants.STR_TOKEN;
				return;
			} else if (isArray(word)) {
				token.type = Constants.ARRAY_TOKEN;
				return;
			} else if (isMap(word)) {
				token.type = Constants.MAP_TOKEN;
				return;
			} else if (isInvoke(word)) {
				token.type = getInvokeTokenType(word);
				return;
			} else if (isVariable(word)) {
				token.type = getVarTokenType(word);
				return;
			}
			token.type = Constants.UNKNOWN;
			return;
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
		if (INVOKE_LOCAL_PATTERN.matcher(word).matches()) {
			return Constants.INVOKE_LOCAL_TOKEN;
		}
		if (INVOKE_FLUENT_PATTERN.matcher(word).matches()) {
			return Constants.INVOKE_FLUENT_TOKEN;
		}
		if (CAST_PATTERN.matcher(word).matches()) {
			return Constants.CAST_TOKEN;
		}

		return Constants.UNKNOWN;
	}

	private static String getVarTokenType(String word) {

		if (MEMBER_VAR_PATTERN.matcher(word).matches()) {// 构造函数
			return Constants.MEMBER_VAR_TOKEN;
		}
		return Constants.VAR_TOKEN;
	}

	private static void getTokenValue(Token token, String word) {

		if (token.isArray()) {// 如果是数组,则解析子语句
			String str = word.substring(1, word.length() - 1);
			List<String> subWords = LexicalAnalyzer.getWords(str);
			subWords.add(0, "[");
			subWords.add("]");
			// 获取tokens
			List<Token> subTokens = getTokens(null, subWords);
			// 生成子语句
			token.value = new Stmt(word, subWords, subTokens);
			return;

		} else if (token.isMap()) {
			String str = word.substring(1, word.length() - 1);
			List<String> subWords = LexicalAnalyzer.getWords(str);
			subWords.add(0, "{");
			subWords.add("}");
			// 获取tokens
			List<Token> subTokens = getTokens(null, subWords);
			// 生成子语句
			token.value = new Stmt(word, subWords, subTokens);
			return;

		} else if (token.isInvoke()) {
			String prefix = word.substring(0, word.indexOf("("));
			String str = word.substring(word.indexOf("(") + 1, word.lastIndexOf(")"));
			List<String> subWords = LexicalAnalyzer.getWords(str);
			subWords.add(0, "(");
			subWords.add(")");
			// 获取tokens
			List<Token> subTokens = getTokens(null, subWords);
			// 追加一个元素在头部
			subTokens.add(0, new Token(Constants.PREFIX_TOKEN, prefix, null));
			// 生成子语句
			token.value = new Stmt(word, subWords, subTokens);
			return;
		}

		token.value = word;
		return;

	}

	private static void getAttachments(Token token, String word) {

		if (token.isInvokeInit()) {// 构造方法
			token.setInitMethodNameAtt(getInitMethodName(word));

		} else if (token.isInvokeStatic()) {// 静态方法调用
			token.setClassNameAtt(getClassName(word));
			token.setStaticMethodNameAtt(getStaticMethodName(word));

		} else if (token.isInvokeMember()) {// 成员方法调用
			token.setVarNameAtt(getVarName(word));
			token.setMemberMethodNameAtt(getMemberMethodName(word));

		} else if (token.isInvokeLocal()) {// 本地方法调用
			token.setLocalMethodNameAtt(getLocalMethodName(word));

		} else if (token.isMemberVar()) {// 成员变量
			token.setVarNameAtt(getVarName(word));
			token.setMemberVarNameAtt(getMemberVarName(word));

		} else if (token.isCast()) {// 强制类型转换
			token.setTypeAtt(new Type(getCastType(word)));
		}

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

	private static String getClassName(String word) {
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

	private static Object getLocalMethodName(String word) {
		return word.substring(0, word.indexOf("("));
	}

	private static String getMemberVarName(String word) {
		return word.substring(word.indexOf(".") + 1, word.length());
	}

	private static String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

}
