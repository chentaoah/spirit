package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
	private static final Pattern CLASS_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");

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
		} else if ("declare".equals(syntax)) {// 类型声明
			tokens.add(new Token("type", words.get(0), null));
			tokens.add(new Token("var", words.get(1), null));
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
			return "keyword";
		} else if (isSeparator(word)) {
			return "separator";
		} else {
			return "keyword_param";
		}
	}

	public static String getTokenType(String word) {

		if (isKeyword(word)) {// 关键字
			return "keyword";
		} else if (isOperator(word)) {// 是否操作符
			return "operator";
		} else if (isSeparator(word)) {// 是否分隔符
			return "separator";
		} else {// 是否一些值
			String type = "unknown";
			if (isNull(word)) {
				type = "null";
			} else if (isBoolean(word)) {
				type = "boolean";
			} else if (isInt(word)) {
				type = "int";
			} else if (isDouble(word)) {
				type = "double";
			} else if (isStr(word)) {
				type = "str";
			} else if (isArray(word)) {
				type = "array";
			} else if (isMap(word)) {
				type = "map";
			} else if (isInvoke(word)) {
				type = getInvokeType(word);
			} else if (isVariable(word)) {// 变量
				type = getVarType(word);
			}
			return type;
		}

	}

	private static Object getTokenValue(String type, String word) {

		if ("array".equals(type)) {// 如果是数组,则解析子语句
			String str = word.substring(1, word.length() - 1);
			List<String> words = LexicalAnalyzer.getWords(str);
			words.add(0, "[");
			words.add("]");
			// 获取tokens
			List<Token> tokens = getTokens(null, words);
			// 生成子语句
			return new Stmt(word, words, null, tokens);

		} else if ("map".equals(type)) {
			String str = word.substring(1, word.length() - 1);
			List<String> words = LexicalAnalyzer.getWords(str);
			words.add(0, "{");
			words.add("}");
			// 获取tokens
			List<Token> tokens = getTokens(null, words);
			// 生成子语句
			return new Stmt(word, words, null, tokens);

		} else if (type.startsWith("invoke_")) {
			String name = word.substring(0, word.indexOf("("));
			String str = word.substring(word.indexOf("(") + 1, word.lastIndexOf(")"));
			List<String> words = LexicalAnalyzer.getWords(str);
			words.add(0, "(");
			words.add(")");
			// 获取tokens
			List<Token> tokens = getTokens(null, words);
			// 追加一个元素在头部
			tokens.add(0, new Token("invoke_name", name, null));
			// 生成子语句
			return new Stmt(word, words, null, tokens);
		}

		return word;
	}

	private static Map<String, String> getAttachments(String word, String type, Object value) {
		Map<String, String> attachments = new HashMap<>();
		if ("invoke_init".equals(type)) {
			attachments.put("init_method_name", getInitMethod(word));
		} else if ("invoke_static".equals(type)) {
			// TODO 静态方法附加参数
		} else if ("invoke_member".equals(type)) {
			// TODO 成员方法附加参数
		}
		return attachments;
	}

	private static boolean isKeywordSyntax(String word) {
		for (String syntax : SYNTAXS) {
			if (syntax.equals(word)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isKeyword(String word) {
		for (String keyword : KEYWORDS) {
			if (keyword.equals(word)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOperator(String word) {
		for (String operator : OPERATORS) {
			if (operator.equals(word)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSeparator(String word) {
		for (String separator : SEPARATORS) {
			if (separator.equals(word)) {
				return true;
			}
		}
		return false;
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

	private static String getInvokeType(String word) {

		if (INVOKE_INIT_PATTERN.matcher(word).matches()) {// 构造函数
			return "invoke_init";
		}
		if (INVOKE_STATIC_PATTERN.matcher(word).matches()) {
			return "invoke_static";
		}
		if (INVOKE_MEMBER_PATTERN.matcher(word).matches()) {
			return "invoke_member";
		}
		return "unknown";
	}

	private static String getVarType(String word) {

		if (VAR_MEMBER_PATTERN.matcher(word).matches()) {// 构造函数
			return "var_member";
		}
		return "var";
	}

	public static boolean isClass(String word) {
		return CLASS_PATTERN.matcher(word).matches();
	}

	public static String getInitMethod(String word) {
		return word.substring(0, word.indexOf("("));
	}

}
