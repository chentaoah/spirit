package com.sum.shy.core.lexical;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.utils.ArrayUtils;

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

	// 关键字
	public static final String[] KEYWORDS = new String[] { "package", "import", "interface", "abstract", "class",
			"extends", "impl", "func", "throws", "if", "else", "for", "in", "do", "while", "try", "catch", "finally",
			"sync", "return", "continue", "break", "throw", "instanceof", "print", "debug", "error" };
	// 操作符
	public static final String[] OPERATORS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "=", "+", "-", "*", "/",
			"%", "<", ">", "!", "++", "--", "<<", "?" };
	// 分隔符
	public static final String[] SEPARATORS = new String[] { "[", "]", "{", "}", "(", ")", ":", ",", ";" };

	// ============================== 注解 ================================

	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[A-Z]+\\w+(\\([\\s\\S]+\\))?$");

	// ============================== 类型 ================================

	public static final String TYPE_ENUM = "boolean|char|short|int|long|float|double|byte|"
			+ "Boolean|Character|Short|Integer|Long|Float|Double|Byte|" + "Object|String";// 类型枚举
	public static final Pattern BASIC_TYPE_PATTERN = Pattern.compile("^(void|" + TYPE_ENUM + ")$");// 基本类型
	public static final Pattern BASIC_TYPE_ARRAY_PATTERN = Pattern.compile("^(" + TYPE_ENUM + ")\\[\\]$");// 基本类型数组
	public static final Pattern TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w+$");// 普通类型
	public static final Pattern TYPE_ARRAY_PATTERN = Pattern.compile("^[A-Z]+\\w+\\[\\]$");// 类型数组
	public static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^[A-Z]+\\w+<[\\s\\S]+>$");// 泛型

	// ============================== 赋值 ================================

	public static final Pattern BASIC_TYPE_ARRAY_INIT_PATTERN = Pattern.compile("^(" + TYPE_ENUM + ")\\[\\d+\\]$");// 基础类型数组声明
	public static final Pattern TYPE_ARRAY_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w+\\[\\d+\\]$");// 类型数组声明
	public static final Pattern TYPE_INIT_PATTERN = Pattern.compile("^[A-Z]+\\w+(<[\\s\\S]+>)?\\([\\s\\S]*\\)$");// 构造方法
	public static final Pattern NULL_PATTERN = Pattern.compile("^null$");
	public static final Pattern BOOL_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern LONG_PATTERN = Pattern.compile("^\\d+L$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern LIST_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");

	// ============================== 表达式 ================================

	public static final Pattern SUBEXPRESS_PATTERN = Pattern.compile("^\\([\\s\\S]+\\)$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^[a-z]+[a-zA-Z0-9]*$");
	public static final Pattern INVOKE_LOCAL_PATTERN = Pattern.compile("^[a-z]+[a-zA-Z0-9]*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_FIELD_PATTERN = Pattern.compile("^\\.[a-z]+[a-zA-Z0-9]*$");
	public static final Pattern INVOKE_METHOD_PATTERN = Pattern.compile("^\\.[a-z]+[a-zA-Z0-9]*\\([\\s\\S]*\\)$");
	public static final Pattern VISIT_ARRAY_INDEX_PATTERN = Pattern.compile("^\\.[a-z]+[a-zA-Z0-9]*\\[\\d+\\]$");
	public static final Pattern ARRAY_INDEX_PATTERN = Pattern.compile("^[a-z]+[a-zA-Z0-9]*\\[\\d+\\]$");

	/**
	 * 获取结构体语义
	 * 
	 * @param syntax
	 * @param words
	 * @return
	 */
	public static List<Token> getStructTokens(List<String> words) {
		List<Token> tokens = new ArrayList<>();
		// 关键字语句特殊处理
		for (String word : words) {
			Token token = new Token();
			getStructTokenType(token, word);
			token.value = word;
			tokens.add(token);
		}
		return tokens;

	}

	/**
	 * 语义分析
	 * 
	 * @param syntax
	 * @param words
	 * @return
	 */
	public static List<Token> getTokens(List<String> words) {
		List<Token> tokens = new ArrayList<>();
		for (String word : words)
			tokens.add(getToken(word));// 一般处理方式
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

	public static void getStructTokenType(Token token, String word) {
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

		if (isAnnotation(word)) {
			token.type = Constants.ANNOTATION_TOKEN;
			return;
		} else if (isKeyword(word)) {// 关键字
			token.type = Constants.KEYWORD_TOKEN;
			return;
		} else if (isOperator(word)) {// 是否操作符
			token.type = Constants.OPERATOR_TOKEN;
			return;
		} else if (isSeparator(word)) {// 是否分隔符
			token.type = Constants.SEPARATOR_TOKEN;
			return;
		} else if (isType(word)) {// 是否类型说明
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
		} else if (isAccess(word)) {// 属性访问
			token.type = getAccessTokenType(word);
			return;
		}
		token.type = Constants.UNKNOWN;
		return;

	}

	private static boolean isAnnotation(String word) {
		return ANNOTATION_PATTERN.matcher(word).matches();
	}

	public static boolean isKeyword(String word) {
		return ArrayUtils.contain(KEYWORDS, word);
	}

	public static boolean isOperator(String word) {
		return ArrayUtils.contain(OPERATORS, word);
	}

	public static boolean isSeparator(String word) {
		return ArrayUtils.contain(SEPARATORS, word);
	}

	public static boolean isType(String word) {
		return BASIC_TYPE_PATTERN.matcher(word).matches() || BASIC_TYPE_ARRAY_PATTERN.matcher(word).matches()
				|| TYPE_PATTERN.matcher(word).matches() || TYPE_ARRAY_PATTERN.matcher(word).matches()
				|| GENERIC_TYPE_PATTERN.matcher(word).matches();
	}

	public static boolean isInit(String word) {
		return BASIC_TYPE_ARRAY_INIT_PATTERN.matcher(word).matches() || TYPE_ARRAY_INIT_PATTERN.matcher(word).matches()
				|| TYPE_INIT_PATTERN.matcher(word).matches();
	}

	public static String getInitTokenType(String word) {
		if (BASIC_TYPE_ARRAY_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (TYPE_ARRAY_INIT_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INIT_TOKEN;
		if (TYPE_INIT_PATTERN.matcher(word).matches())
			return Constants.TYPE_INIT_TOKEN;
		return Constants.UNKNOWN;
	}

	public static boolean isValue(String word) {
		return NULL_PATTERN.matcher(word).matches() || BOOL_PATTERN.matcher(word).matches()
				|| INT_PATTERN.matcher(word).matches() || DOUBLE_PATTERN.matcher(word).matches()
				|| STR_PATTERN.matcher(word).matches() || LIST_PATTERN.matcher(word).matches()
				|| MAP_PATTERN.matcher(word).matches();
	}

	public static String getValueTokenType(String word) {
		if (NULL_PATTERN.matcher(word).matches())
			return Constants.NULL_TOKEN;
		if (BOOL_PATTERN.matcher(word).matches())
			return Constants.BOOL_TOKEN;
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
		return Constants.UNKNOWN;
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
		return INVOKE_LOCAL_PATTERN.matcher(word).matches() || VISIT_FIELD_PATTERN.matcher(word).matches()
				|| INVOKE_METHOD_PATTERN.matcher(word).matches() || VISIT_ARRAY_INDEX_PATTERN.matcher(word).matches()
				|| ARRAY_INDEX_PATTERN.matcher(word).matches();
	}

	public static String getAccessTokenType(String word) {
		if (INVOKE_LOCAL_PATTERN.matcher(word).matches())
			return Constants.INVOKE_LOCAL_TOKEN;
		if (VISIT_FIELD_PATTERN.matcher(word).matches())
			return Constants.VISIT_FIELD_TOKEN;
		if (INVOKE_METHOD_PATTERN.matcher(word).matches())
			return Constants.INVOKE_METHOD_TOKEN;
		if (VISIT_ARRAY_INDEX_PATTERN.matcher(word).matches())
			return Constants.VISIT_ARRAY_INDEX_TOKEN;
		if (ARRAY_INDEX_PATTERN.matcher(word).matches())
			return Constants.ARRAY_INDEX_TOKEN;
		return Constants.UNKNOWN;
	}

	public static boolean isDouble(String word) {
		return DOUBLE_PATTERN.matcher(word).matches();
	}

	public static void getTokenValue(Token token, String word) {

		if (token.isType()) {
			token.value = getTypeStmtIfNeed(word);
			return;

		} else if (token.isArrayInit() || token.isList()) {// 这里的拆分是为了更好的加上new这个关键字
			token.value = getSubStmt(word, "[", "]");
			return;

		} else if (token.isMap()) {
			token.value = getSubStmt(word, "{", "}");
			return;

		} else if (token.isSubexpress() || token.isInvoke()) {
			token.value = getSubStmt(word, "(", ")");
			return;

		}
		token.value = word;
		return;
	}

	public static Object getTypeStmtIfNeed(String word) {
		if (word.contains("<") && word.contains(">")) {
			Stmt subStmt = getSubStmt(word, "<", ">");
			int count = 0;
			for (Token subToken : subStmt.tokens) {
				if ("?".equals(subToken.toString()))
					subStmt.tokens.set(count, new Token(Constants.TYPE_TOKEN, "?"));
				count++;
			}
			return subStmt;
		}
		return word;
	}

	public static Stmt getSubStmt(String word, String left, String right) {
		// 开始位置
		int start = word.indexOf(left);
		// 结束位置
		int end = word.lastIndexOf(right);
		// 前缀,这里兼容了泛型的类型声明
		Object prefix = start != 0 ? getTypeStmtIfNeed(word.substring(0, start)) : null;
		// 内容
		String content = word.substring(start + 1, end);
		// 分解内容
		List<String> subWords = LexicalAnalyzer.getWords(content);
		// 获取tokens
		List<Token> subTokens = getTokens(subWords);
		// 插入分隔符
		subTokens.add(0, new Token(Constants.SEPARATOR_TOKEN, left));// 注意:这个符号不再是操作符,而是分隔符
		subTokens.add(new Token(Constants.SEPARATOR_TOKEN, right));// 20191213 ct 修复>分隔符插入位置错误的问题
		// 追加一个元素在头部
		if (prefix != null)
			subTokens.add(0, new Token(Constants.PREFIX_TOKEN, prefix));
		// 生成子语句
		return new Stmt(word, subTokens);
	}

	public static void getAttachments(Token token, String word) {

		if (token.isArrayInit()) {// 数组构造
			token.setTypeNameAtt(getArrayInitType(word));
			return;

		} else if (token.isTypeInit()) {// 构造
			token.setTypeNameAtt(getMemberName(word));
			return;

		} else if (token.isCast()) {// 强制类型转换
			token.setTypeNameAtt(getCastType(word));
			return;

		} else if (token.isAccess()) {// 属性访问
			token.setMemberNameAtt(getMemberName(word));
			return;

		}

	}

	public static String getArrayInitType(String word) {
		return word.substring(0, word.indexOf("[")) + "[]";
	}

	public static String getCastType(String word) {
		return word.substring(1, word.length() - 1);
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
