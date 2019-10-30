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

	// 关键字
	public static final String[] KEYWORDS = new String[] { "package", "import", "def", "class", "func", "return" };

	// 操作符
	public static final String[] OPERATORS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "=", "+", "-", "*", "/",
			"%", "<", ">" };
	// 分隔符
	public static final String[] SEPARATORS = new String[] { "[", "]", "{", "}", "(", ")", ":", "," };

	public static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
	public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
	public static final Pattern STR_PATTERN = Pattern.compile("^\"[\\s\\S]*\"$");
	public static final Pattern INVOKE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_INIT_PATTERN = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_STATIC_PATTERN = Pattern
			.compile("^[A-Z]+[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern INVOKE_MEMBER_PATTERN = Pattern.compile("^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\([\\s\\S]*\\)$");
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[\\s\\S]*\\]$");
	public static final Pattern MAP_PATTERN = Pattern.compile("^\\{[\\s\\S]*\\}$");
	public static final Pattern VAR_PATTERN = Pattern.compile("^(?!\\d+$)[a-zA-Z0-9]+$");

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static String getTypeByStmt(Stmt stmt) {
		return getType(stmt.tokens);
	}

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static String getTypeByWords(List<String> words) {
		return getType(getTokens(words));
	}

	/**
	 * 类型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static String getType(List<Token> tokens) {
		for (Token token : tokens) {
			String type = token.type;
			if ("boolean".equals(type)) {
				return "boolean";
			} else if ("int".equals(type)) {
				return "int";
			} else if ("double".equals(type)) {
				return "double";
			} else if ("str".equals(type)) {
				return "str";
			} else if (type.startsWith("invoke_init")) {
				return token.attachments.get("init_method_name");
			} else if ("array".equals(type)) {
				return "array";
			} else if ("map".equals(type)) {
				return "map";
			}
		}
		return "unknown";
	}

	/**
	 * 泛型推断
	 * 
	 * @param stmt
	 * @return
	 */
	public static List<String> getGenericTypes(Stmt stmt) {
		List<String> genericTypes = new ArrayList<>();
		// 开始遍历最外层,看下是否是array或者map
		for (Token token : stmt.tokens) {
			String type = token.type;
			if ("array".equals(type)) {
				// 获取子语句
				Stmt subStmt = (Stmt) token.value;
				// 开始遍历里面的那层
				for (Token subToken : subStmt.tokens) {
					String subType = getGenericType(subToken);
					if (!"unknown".equals(subType)) {
						genericTypes.add(subType);
						return genericTypes;
					}
				}
			} else if ("map".equals(type)) {
				// 获取子语句
				Stmt subStmt = (Stmt) token.value;
				// 开始遍历里面的那层
				boolean flag = true;
				genericTypes.add("unknown");
				genericTypes.add("unknown");
				for (Token subToken : subStmt.tokens) {
					if (":".equals(subToken.value)) {
						flag = false;
					} else if (",".equals(subToken.value)) {
						flag = true;
					}
					String subType = getGenericType(subToken);
					if (!"unknown".equals(subType)) {
						genericTypes.set(flag ? 0 : 1, subType);
						if (!"unknown".equals(genericTypes.get(0)) && !"unknown".equals(genericTypes.get(1))) {
							return genericTypes;
						}
					}
				}
			}
		}
		return genericTypes;
	}

	public static String getGenericType(Token subToken) {
		String subType = subToken.type;
		if ("boolean".equals(subType)) {
			return "boolean";
		} else if ("int".equals(subType)) {
			return "int";
		} else if ("double".equals(subType)) {
			return "double";
		} else if ("str".equals(subType)) {
			return "str";
		} else if ("invoke_init".equals(subType)) {
			return subToken.attachments.get("init_method_name");
		}
		return "unknown";
	}

	/**
	 * 语义分析
	 * 
	 * @param words
	 * @return
	 */
	public static List<Token> getTokens(List<String> words) {
		List<Token> tokens = new ArrayList<>();
		for (String word : words) {
			// 类型
			String type = getTokenType(word);
			// 值
			Object value = getTokenValue(type, word);
			// 附加信息
			Map<String, String> attachments = getAttachments(word, type, value);

			tokens.add(new Token(type, value, attachments));
		}
		return tokens;
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
			if (isBoolean(word)) {
				type = "boolean";
			} else if (isInt(word)) {
				type = "int";
			} else if (isDouble(word)) {
				type = "double";
			} else if (isStr(word)) {
				type = "str";
			} else if (isInvoke(word)) {
				type = getInvokeType(word);
			} else if (isArray(word)) {
				type = "array";
			} else if (isMap(word)) {
				type = "map";
			} else if (isVariable(word)) {// 变量
				type = "var";
			}
			return type;
		}

	}

	private static Object getTokenValue(String type, String word) {

		if ("array".equals(type)) {// 如果是数组,则解析子语句
			String str = word.substring(1, word.length() - 1);
			List<String> words = LexicalAnalyzer.analysis(str);
			words.add(0, "[");
			words.add(words.size() - 1, "]");
			// 获取tokens
			List<Token> tokens = getTokens(words);
			// 生成子语句
			return new Stmt(word, null, tokens);

		} else if ("map".equals(type)) {
			String str = word.substring(1, word.length() - 1);
			List<String> words = LexicalAnalyzer.analysis(str);
			words.add(0, "{");
			words.add(words.size() - 1, "}");
			// 获取tokens
			List<Token> tokens = getTokens(words);
			// 生成子语句
			return new Stmt(word, null, tokens);

		} else if (type.startsWith("invoke_")) {
			String name = word.substring(0, word.indexOf("("));
			String str = word.substring(word.indexOf("(") + 1, word.lastIndexOf(")"));
			List<String> words = LexicalAnalyzer.analysis(str);
			words.add(1, "(");
			words.add(words.size() - 1, ")");
			// 获取tokens
			List<Token> tokens = getTokens(words);
			// 追加一个元素在头部
			tokens.add(0, new Token("invoke_name", name, null));
			// 生成子语句
			return new Stmt(word, null, tokens);
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

	public static String getInitMethod(String word) {
		return word.substring(0, word.indexOf("("));
	}

}
