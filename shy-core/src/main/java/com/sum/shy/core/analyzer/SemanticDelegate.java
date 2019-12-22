package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;
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
	public static final String[] SYNTAXS = new String[] { "package", "import", "interface", "abstract", "class",
			"func" };
	// 关键字
	public static final String[] KEYWORDS = new String[] { "package", "import", "interface", "abstract", "class",
			"extends", "impl", "func", "throws", "if", "else", "for", "in", "do", "while", "try", "catch", "sync",
			"return", "continue", "break", "throw", "instanceof", "print", "debug", "error" };
	// 操作符
	public static final String[] OPERATORS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "=", "+", "-", "*", "/",
			"%", "<", ">", "!", "++", "--", "<<", "?" };
	// 分隔符
	public static final String[] SEPARATORS = new String[] { "[", "]", "{", "}", "(", ")", ":", ",", ";" };

	// ============================== 类型判断 ================================

	// 基础类型
	public static final Pattern BASIC_TYPE_PATTERN = Pattern.compile(
			"^(void|boolean|char|short|int|long|float|double|byte|Boolean|Character|Short|Integer|Long|Float|Double|Byte|Object|String)$");
	// 基础类型数组
	public static final Pattern BASIC_TYPE_ARRAY_PATTERN = Pattern.compile(
			"^(boolean|char|short|int|long|float|double|byte|Boolean|Character|Short|Integer|Long|Float|Double|Byte|Object|String)\\[\\]$");

	// 类型--Father and G_Father
	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w+$");
	// 数组--Father[] and G_Father[]
	public static final Pattern TYPE_ARRAY_PATTERN = Pattern.compile("^[A-Z]+\\w+\\[\\]$");
	// 泛型--Father<Child> and G_Father<Child>
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w+<[\\s\\S]+>$");

	// ============================== 初始化 ================================

	// 构造方法(支持别名)
	public static final Pattern TYPE_INIT_PATTERN = Pattern.compile("^[A-Z]+[\\w<>]+\\([\\s\\S]*\\)$");
	// 基础类型数组声明
	public static final Pattern BASIC_TYPE_ARRAY_INIT_PATTERN = Pattern.compile(
			"^(boolean|char|short|int|long|float|double|byte|Boolean|Character|Short|Integer|Long|Float|Double|Byte|Object|String)\\[\\d+\\]$");
	// 类型数组声明
	public static final Pattern TYPE_ARRAY_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w+\\[\\d+\\]$");

	// ============================== 字面值判断 ================================

	public static final Pattern BOOL_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern LIST_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");

	// ============================== 子表达式 ================================

	// 子表达式--里面是type则是cast 其他则为表达式
	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");

	// ============================== 变量判断 ================================

	public static final Pattern VAR_PATTERN = Pattern.compile("^[a-z]+[a-zA-Z0-9]*$");

	// ============================== 方法调用 ================================

	// 本地方法
	public static final Pattern INVOKE_LOCAL_PATTERN = Pattern.compile("^[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	// 流式成员变量
	public static final Pattern VISIT_MEMBER_PATTERN = Pattern.compile("^\\.[a-zA-Z0-9]+$");
	// 流式调用
	public static final Pattern INVOKE_MEMBER_PATTERN = Pattern.compile("^\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");

	// ============================== 其他 ================================

	// 快速索引
	public static final Pattern QUICK_INDEX_PATTERN = Pattern.compile("^(\\.)?[a-z]+[a-zA-Z0-9]*\\[\\d+\\]$");

	/**
	 * 语义分析
	 * 
	 * @param syntax
	 * @param words
	 * @return
	 */
	public static List<Token> getTokens(String syntax, List<String> words) {

		List<Token> tokens = new ArrayList<>();

		// 注解
		if (Constants.ANNOTATION_SYNTAX.equals(syntax)) {
			tokens.add(new Token(Constants.ANNOTATION_TOKEN, words.get(0), null));
			return tokens;
		}

		// 关键字语句特殊处理
		if (isKeywordSyntax(syntax)) {
			for (String word : words) {
				Token token = new Token();
				getKeywordTokenType(token, word);
				token.value = word;
				tokens.add(token);
			}
			return tokens;
		}

		for (String word : words) {
			tokens.add(getToken(word));
		}
		return tokens;

	}

	/**
	 * 生成一个token
	 * 
	 * @param word
	 * @return
	 */
	public static Token getToken(String word) {
		Token token = new Token();
		getTokenType(token, word);
		getTokenValue(token, word);
		getAttachments(token, word);
		return token;
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
		} else {
			if (isType(word)) {// 是否类型说明
				token.type = Constants.TYPE_TOKEN;
				return;
			} else if (isInit(word)) {// 初始化
				token.type = getInitTokenType(word);
				return;
			} else if (isValue(word)) {// 字面值
				token.type = getValueTokenType(word);
				return;
			} else if (isSubexpress(word)) {// 子表达式
				token.type = getSubexpressTokenType(word);
				return;
			} else if (isVar(word)) {// 变量
				token.type = Constants.VAR_TOKEN;
				return;
			} else if (isInvoke(word)) {// 方法调用
				token.type = getInvokeTokenType(word);
				return;
			} else if (isQuickIndex(word)) {
				token.type = Constants.QUICK_INDEX_TOKEN;
				return;
			}
			token.type = Constants.UNKNOWN;
			return;
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

	public static boolean isType(String word) {
		return BASIC_TYPE_PATTERN.matcher(word).matches() || BASIC_TYPE_ARRAY_PATTERN.matcher(word).matches()
				|| TYPE_PATTERN.matcher(word).matches() || TYPE_ARRAY_PATTERN.matcher(word).matches()
				|| GENERIC_TYPE_PATTERN.matcher(word).matches();
	}

	private static boolean isInit(String word) {
		return BASIC_TYPE_ARRAY_INIT_PATTERN.matcher(word).matches() || TYPE_ARRAY_INIT_PATTERN.matcher(word).matches()
				|| TYPE_INIT_PATTERN.matcher(word).matches();
	}

	private static String getInitTokenType(String word) {
		if (BASIC_TYPE_ARRAY_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (TYPE_ARRAY_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (TYPE_INIT_PATTERN.matcher(word).matches())
			return Constants.TYPE_INIT_TOKEN;
		return Constants.UNKNOWN;
	}

	private static boolean isValue(String word) {
		return "null".equals(word) || BOOL_PATTERN.matcher(word).matches() || INT_PATTERN.matcher(word).matches()
				|| DOUBLE_PATTERN.matcher(word).matches() || STR_PATTERN.matcher(word).matches()
				|| LIST_PATTERN.matcher(word).matches() || MAP_PATTERN.matcher(word).matches();
	}

	public static boolean isDouble(String word) {
		return DOUBLE_PATTERN.matcher(word).matches();
	}

	private static String getValueTokenType(String word) {
		if ("null".equals(word))
			return Constants.NULL_TOKEN;
		if (BOOL_PATTERN.matcher(word).matches())
			return Constants.BOOL_TOKEN;
		if (INT_PATTERN.matcher(word).matches())
			return Constants.INT_TOKEN;
		if (DOUBLE_PATTERN.matcher(word).matches())
			return Constants.DOUBLE_TOKEN;
		if (STR_PATTERN.matcher(word).matches())
			return Constants.STR_TOKEN;
		if (LIST_PATTERN.matcher(word).matches())
			return Constants.LIST_TOKEN;
		if (MAP_PATTERN.matcher(word).matches())
			return Constants.MAP_TOKEN;
		return Constants.UNKNOWN;
	}

	private static boolean isSubexpress(String word) {
		return SUBEXPRESS_PATTERN.matcher(word).matches();
	}

	private static String getSubexpressTokenType(String word) {
		if (isType(getCastType(word)))
			return Constants.CAST_TOKEN;
		return Constants.SUBEXPRESS_TOKEN;
	}

	public static boolean isVar(String word) {
		return VAR_PATTERN.matcher(word).matches();
	}

	public static boolean isInvoke(String word) {
		return INVOKE_LOCAL_PATTERN.matcher(word).matches() || VISIT_MEMBER_PATTERN.matcher(word).matches()
				|| INVOKE_MEMBER_PATTERN.matcher(word).matches();
	}

	private static String getInvokeTokenType(String word) {
		if (INVOKE_LOCAL_PATTERN.matcher(word).matches())
			return Constants.INVOKE_LOCAL_TOKEN;
		if (VISIT_MEMBER_PATTERN.matcher(word).matches())
			return Constants.VISIT_FIELD_TOKEN;
		if (INVOKE_MEMBER_PATTERN.matcher(word).matches())
			return Constants.INVOKE_METHOD_TOKEN;
		return Constants.UNKNOWN;
	}

	public static boolean isQuickIndex(String word) {
		return QUICK_INDEX_PATTERN.matcher(word).matches();
	}

	private static void getTokenValue(Token token, String word) {

		if (token.isType()) {
			// 如果是泛型,则进行深度的拆分
			if (word.contains("<") && word.contains(">")) {
				String prefix = word.substring(0, word.indexOf("<"));
				String str = word.substring(word.indexOf("<") + 1, word.lastIndexOf(">"));
				List<String> subWords = LexicalAnalyzer.getWords(str);
				// 获取tokens
				List<Token> subTokens = getTokens(null, subWords);
				// 追加一个元素在头部
				subTokens.add(0, new Token(Constants.PREFIX_TOKEN, prefix, null));
				subTokens.add(1, new Token(Constants.SEPARATOR_TOKEN, "<", null));// 注意:这个符号不再是操作符,而是分隔符
				subTokens.add(new Token(Constants.SEPARATOR_TOKEN, ">", null));// 20191213 ct 修复>分隔符插入位置错误的问题
				// 将泛型中的?替换一下
				int count = 0;
				for (Token subToken : subTokens) {
					if ("?".equals(subToken.value.toString()))
						subTokens.set(count, new Token(Constants.TYPE_TOKEN, "?", null));
					count++;
				}
				// 生成子语句
				token.value = new Stmt(word, subWords, subTokens);
				return;
			}
			token.value = word;
			return;

		} else if (token.isList()) {// 如果是数组,则解析子语句
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

		} else if (token.isArrayInit()) {// 这里的拆分是为了更好的加上new这个关键字
			String prefix = word.substring(0, word.indexOf("["));
			String number = word.substring(word.indexOf("[") + 1, word.indexOf("]"));
			List<String> subWords = new ArrayList<>();
			subWords.add(0, "[");
			subWords.add(number);
			subWords.add("]");
			// 获取tokens
			List<Token> subTokens = getTokens(null, subWords);
			// 追加一个元素在头部
			subTokens.add(0, new Token(Constants.PREFIX_TOKEN, prefix, null));
			// 生成子语句
			token.value = new Stmt(word, subWords, subTokens);
			return;

		} else if (token.isSubexpress()) {
			String str = word.substring(word.indexOf("(") + 1, word.lastIndexOf(")"));
			List<String> subWords = LexicalAnalyzer.getWords(str);
			subWords.add(0, "(");
			subWords.add(")");
			// 获取tokens
			List<Token> subTokens = getTokens(null, subWords);
			// 生成子语句
			token.value = new Stmt(word, subWords, subTokens);
			return;

		}

		token.value = word;
		return;

	}

	private static void getAttachments(Token token, String word) {

		if (token.isType()) {
			token.setTypeNameAtt(word);
			return;

		} else if (token.isArrayInit()) {// 强制类型转换
			token.setTypeNameAtt(getArrayInitType(word));
			return;

		} else if (token.isTypeInit()) {// 构造方法
			token.setTypeNameAtt(getMethodName(word));
			return;

		} else if (token.isCast()) {// 强制类型转换
			token.setTypeNameAtt(getCastType(word));
			return;

		} else if (token.isInvokeLocal()) {// 本地方法调用
			token.setMemberNameAtt(getMethodName(word));
			return;

		} else if (token.isVisitField()) {// 访问成员变量
			token.setMemberNameAtt(getMemberName(word));
			return;

		} else if (token.isInvokeMethod()) {// 流式调用
			token.setMemberNameAtt(getMemberName(word));
			return;

		} else if (token.isQuickIndex()) {// 流式成员变量
			token.setMemberNameAtt(getMemberName(word));
			return;

		}

	}

	private static String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

	private static String getArrayInitType(String word) {
		return word.substring(0, word.indexOf("[")) + "[]";
	}

	public static String getMethodName(String word) {
		return word.substring(0, word.indexOf("("));
	}

	public static String getMemberName(String word) {
		int start = word.startsWith(".") ? 1 : 0;
		int end = word.length();
		if (word.contains("[")) {
			end = word.indexOf("[");
		} else if (word.contains("(")) {
			end = word.indexOf("(");
		}
		return word.substring(start, end);
	}

}
