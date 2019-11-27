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
	public static final String[] SYNTAXS = new String[] { "package", "import", "class", "func" };

	// 关键字
	public static final String[] KEYWORDS = new String[] { "package", "import", "class", "func", "return", "if", "else",
			"for", "in", "while", "do", "try", "catch", "throw", "print", "debug" };

	// 操作符
	public static final String[] OPERATORS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "=", "+", "-", "*", "/",
			"%", "<", ">", "!", "++", "--", "<<" };
	// 分隔符
	public static final String[] SEPARATORS = new String[] { "[", "]", "{", "}", "(", ")", ":", ",", ";" };

	// ============================== 类型判断 ================================

	public static final Pattern BASIC_TYPE_PATTERN = Pattern
			.compile("^(void|boolean|int|long|double|Boolean|Integer|Long|Double|Object|String)$");// 常用基本类型
	public static final Pattern BASIC_ARRAY_TYPE_PATTERN = Pattern
			.compile("^(boolean\\[\\]|int\\[\\]|long\\[\\]|double\\[\\])$");// 常用基本类型数组
	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");// 普通类型
	public static final Pattern ARRAY_TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\[\\]$");// 数组
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+<[\\s\\S]+>$");// 泛型

	public static final Pattern CAST_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");// 强制转换，还需要type判断

	public static final Pattern INIT_BASIC_ARRAY_TYPE_PATTERN = Pattern
			.compile("^(boolean\\[\\d+\\]|int\\[\\d+\\]|long\\[\\d+\\]|double\\[\\d+\\])$");// 常用基本类型数组
	public static final Pattern INIT_ARRAY_TYPE_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\[\\d+\\]$");// 常用基本类型数组

	// ============================== 字面值判断 ================================

	public static final Pattern BOOL_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");

	// ============================== 方法调用 ================================

	public static final Pattern INVOKE_PATTERN = Pattern.compile("^[a-zA-Z0-9_<>\\.]*\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_INIT_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9_<>]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_STATIC_PATTERN = Pattern
			.compile("^[A-Z]+[a-zA-Z0-9_]+\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_LOCAL_PATTERN = Pattern.compile("^[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_FLUENT_PATTERN = Pattern.compile("^\\.[a-zA-Z0-9\\.]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_MEMBER_PATTERN = Pattern
			.compile("^[a-zA-Z0-9]+\\.[a-zA-Z0-9\\.]+\\([\\s\\S]*\\)$");

	public static final Pattern QUICK_INDEX_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.]+\\[[\\s\\S]+\\]$");// 快速索引

	// ============================== 变量判断 ================================

	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9_\\.]+$");
	public static final Pattern STATIC_VAR_PATTERN = Pattern
			.compile("^(?!\\d+$)[A-Z]+[a-zA-Z0-9_]+\\.[a-zA-Z0-9\\.]+$");
	public static final Pattern MEMBER_VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+\\.[a-zA-Z0-9\\.]+$");
	public static final Pattern MEMBER_VAR_FLUENT_PATTERN = Pattern.compile("^(?!\\d+$)\\.[a-zA-Z0-9]+$");

	// ============================== 其他 ================================

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

		} else {
			for (String word : words) {
				tokens.add(getToken(word));
			}
			// 将fluent串联起来
			Token lastToken = null;
			for (Token token : tokens) {
				if (token.isFluent()) {
					lastToken.setNext(token);
				}
				lastToken = token;
			}

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
			} else if (isCast(word)) {// 类型强制转换
				token.type = Constants.CAST_TOKEN;
				return;
			} else if (isArrayInit(word)) {// 数组初始化
				token.type = Constants.ARRAY_INIT_TOKEN;
				return;
			} else if (isNull(word)) {// 空
				token.type = Constants.NULL_TOKEN;
				return;
			} else if (isBool(word)) {// 布尔值
				token.type = Constants.BOOL_TOKEN;
				return;
			} else if (isInt(word)) {// 整数
				token.type = Constants.INT_TOKEN;
				return;
			} else if (isDouble(word)) {// 浮点数
				token.type = Constants.DOUBLE_TOKEN;
				return;
			} else if (isStr(word)) {// 字符串
				token.type = Constants.STR_TOKEN;
				return;
			} else if (isArray(word)) {// 数组
				token.type = Constants.ARRAY_TOKEN;
				return;
			} else if (isMap(word)) {// 键值对
				token.type = Constants.MAP_TOKEN;
				return;
			} else if (isInvoke(word)) {// 方法调用
				token.type = getInvokeTokenType(word);
				return;
			} else if (isVariable(word)) {// 变量
				token.type = getVarTokenType(word);
				return;
			} else if (isQuickIndex(word)) {
				token.type = Constants.QUICK_INDEX_TOKEN;
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

		return Constants.UNKNOWN;
	}

	private static String getVarTokenType(String word) {

		if (STATIC_VAR_PATTERN.matcher(word).matches()) {
			return Constants.STATIC_VAR_TOKEN;
		}
		if (MEMBER_VAR_PATTERN.matcher(word).matches()) {
			return Constants.MEMBER_VAR_TOKEN;
		}
		if (MEMBER_VAR_FLUENT_PATTERN.matcher(word).matches()) {
			return Constants.MEMBER_VAR_FLUENT_TOKEN;
		}
		return Constants.VAR_TOKEN;
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
				subTokens.add(1, new Token(Constants.SEPARATOR_TOKEN, "<", null));
				subTokens.add(subTokens.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ">", null));
				// 生成子语句
				token.value = new Stmt(word, subWords, subTokens);
				return;
			}
			token.value = word;
			return;

		} else if (token.isArray()) {// 如果是数组,则解析子语句
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
		}

		token.value = word;
		return;

	}

	private static void getAttachments(Token token, String word) {

		if (token.isType()) {
			token.setTypeNameAtt(word);
			return;

		} else if (token.isCast()) {// 强制类型转换
			token.setTypeNameAtt(getCastType(word));
			return;

		} else if (token.isArrayInit()) {// 强制类型转换
			token.setTypeNameAtt(getArrayInitType(word));
			return;

		} else if (token.isInvokeInit()) {// 构造方法
			token.setTypeNameAtt(getInitMethodName(word));
			return;

		} else if (token.isInvokeStatic()) {// 静态方法调用
			token.setTypeNameAtt(getTypeName(word));
			token.setPropertiesAtt(getProperties(word));// 中间可能有很多的成员变量访问
			token.setMethodNameAtt(getMethodName(word));
			return;

		} else if (token.isInvokeMember()) {// 成员方法调用
			token.setVarNameAtt(getVarName(word));
			token.setPropertiesAtt(getProperties(word));// 中间可能有很多的成员变量访问
			token.setMethodNameAtt(getMethodName(word));
			return;

		} else if (token.isInvokeLocal()) {// 本地方法调用
			token.setMethodNameAtt(getLocalMethodName(word));
			return;

		} else if (token.isInvokeFluent()) {// 流式调用
			token.setPropertiesAtt(getProperties(word));// 中间可能有很多的成员变量访问
			token.setMethodNameAtt(getMethodName(word));
			return;

		} else if (token.isStaticVar()) {// 静态变量
			token.setTypeNameAtt(getTypeName(word));
			token.setPropertiesAtt(getProperties(word));
			return;

		} else if (token.isMemberVar()) {// 成员变量
			token.setVarNameAtt(getVarName(word));
			token.setPropertiesAtt(getProperties(word));
			return;

		} else if (token.isMemberVarFluent()) {// 流式成员变量
			token.setPropertiesAtt(getProperties(word));
			return;

		} else if (token.isQuickIndex()) {// 流式成员变量
			token.setVarNameAtt(getArrayVarName(word));
			token.setPropertiesAtt(getArrayProperties(word));
			token.setMethodNameAtt("$quick_index");
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
		return BASIC_TYPE_PATTERN.matcher(word).matches() || BASIC_ARRAY_TYPE_PATTERN.matcher(word).matches()
				|| TYPE_PATTERN.matcher(word).matches() || ARRAY_TYPE_PATTERN.matcher(word).matches()
				|| GENERIC_TYPE_PATTERN.matcher(word).matches();
	}

	private static boolean isCast(String word) {// 必须是两边有括号，并且内部是类型声明
		return CAST_PATTERN.matcher(word).matches() && isType(getCastType(word));
	}

	private static boolean isArrayInit(String word) {
		return INIT_BASIC_ARRAY_TYPE_PATTERN.matcher(word).matches() || INIT_ARRAY_TYPE_PATTERN.matcher(word).matches();
	}

	private static boolean isNull(String word) {
		return "null".equals(word);
	}

	public static boolean isBool(String word) {
		return BOOL_PATTERN.matcher(word).matches();
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

	public static boolean isQuickIndex(String word) {
		return QUICK_INDEX_PATTERN.matcher(word).matches();
	}

	private static String getCastType(String word) {
		return word.substring(1, word.length() - 1);
	}

	private static String getArrayInitType(String word) {
		return word.substring(0, word.indexOf("[")) + "[]";
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

	private static List<String> getProperties(String word) {
		List<String> list = new ArrayList<>();
		if (word.contains("(") && word.contains(")")) {
			String[] strs = word.substring(0, word.indexOf("(")).split("\\.");
			for (int i = 1; i < strs.length - 1; i++) {
				list.add(strs[i]);
			}
		} else {
			String[] strs = word.split("\\.");
			for (int i = 1; i < strs.length; i++) {
				list.add(strs[i]);
			}
		}
		return list;
	}

	private static String getMethodName(String word) {
		return word.substring(word.lastIndexOf(".") + 1, word.indexOf("("));
	}

	private static String getLocalMethodName(String word) {
		return word.substring(0, word.indexOf("("));
	}

	private static String getArrayVarName(String word) {
		String[] names = word.substring(0, word.indexOf("[")).split("\\.");
		return names[0];
	}

	private static List<String> getArrayProperties(String word) {
		List<String> list = new ArrayList<>();
		String[] names = word.substring(0, word.indexOf("[")).split("\\.");
		for (int i = 1; i < names.length; i++) {
			list.add(names[i]);
		}
		return list;
	}

}
