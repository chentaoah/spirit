package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.sum.shy.core.ShyReader;
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
	 * 一般语句的处理方式
	 * 
	 * @param units
	 * @return
	 */
	public static List<Token> getTokens(List<String> units) {
		List<Token> tokens = new ArrayList<>();
		for (String unit : units) {
			// 类型
			String type = getTokenType(unit);
			// 值
			Object value = getTokenValue(type, unit);
			// 附加信息
			Map<String, String> attachments = getAttachments(unit, type, value);

			tokens.add(new Token(type, value, attachments));
		}
		return tokens;
	}

	public static String getType(List<Token> tokens) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<String> getGenericTypes(List<Token> tokens) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getTokenType(String str) {

		if (isOperator(str)) {// 是否操作符
			return "operator";
		} else if (isSeparator(str)) {// 是否分隔符
			return "separator";
		} else {// 是否一些值
			String type = "unknown";
			if (isBoolean(str)) {
				type = "boolean";
			} else if (isInt(str)) {
				type = "int";
			} else if (isDouble(str)) {
				type = "double";
			} else if (isStr(str)) {
				type = "str";
			} else if (isInvoke(str)) {
				type = getInvokeType(str);
			} else if (isArray(str)) {
				type = "array";
			} else if (isMap(str)) {
				type = "map";
			} else if (isVariable(str)) {// 变量
				type = "var";
			}
			return type;
		}

	}

	private static Object getTokenValue(String type, String unit) {

		if ("array".equals(type)) {// 如果是数组,则解析子语句
			String str = unit.substring(1, unit.length() - 1);
			List<String> units = LexicalAnalyzer.analysis(str);
			units.add(0, "[");
			units.add(units.size() - 1, "]");
			// 获取tokens
			List<Token> tokens = getTokens(units);
			// 生成子语句
			return new Stmt(unit, null, tokens);

		} else if ("map".equals(type)) {
			String str = unit.substring(1, unit.length() - 1);
			List<String> units = LexicalAnalyzer.analysis(str);
			units.add(0, "{");
			units.add(units.size() - 1, "}");
			// 获取tokens
			List<Token> tokens = getTokens(units);
			// 生成子语句
			return new Stmt(unit, null, tokens);

		} else if (type.startsWith("invoke_")) {
			String name = unit.substring(0, unit.indexOf("("));
			String str = unit.substring(unit.indexOf("(") + 1, unit.lastIndexOf(")"));
			List<String> units = LexicalAnalyzer.analysis(str);
			units.add(1, "(");
			units.add(units.size() - 1, ")");
			// 获取tokens
			List<Token> tokens = getTokens(units);
			// 追加一个元素在头部
			tokens.add(0, new Token("invoke_name", name, null));
			// 生成子语句
			return new Stmt(unit, null, tokens);
		}

		return unit;
	}

	private static Map<String, String> getAttachments(String unit, String type, Object value) {
		Map<String, String> attachments = new HashMap<>();
		if ("invoke_init".equals(type)) {
			attachments.put("init_method_name", getInitMethod(unit));
		} else if ("invoke_static".equals(type)) {
			// TODO 静态方法附加参数
		} else if ("invoke_member".equals(type)) {
			// TODO 成员方法附加参数
		}
		return attachments;
	}

	public static boolean isOperator(String str) {
		for (String operator : OPERATORS) {
			if (operator.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSeparator(String str) {
		for (String separator : SEPARATORS) {
			if (separator.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBoolean(String str) {
		return BOOLEAN_PATTERN.matcher(str).matches();
	}

	public static boolean isInt(String str) {
		return INT_PATTERN.matcher(str).matches();
	}

	public static boolean isDouble(String str) {
		return DOUBLE_PATTERN.matcher(str).matches();
	}

	public static boolean isStr(String str) {
		return STR_PATTERN.matcher(str).matches();
	}

	public static boolean isInvoke(String str) {
		return INVOKE_PATTERN.matcher(str).matches();
	}

	public static boolean isArray(String str) {
		return ARRAY_PATTERN.matcher(str).matches();
	}

	public static boolean isMap(String str) {
		return MAP_PATTERN.matcher(str).matches();
	}

	public static boolean isVariable(String str) {
		return VAR_PATTERN.matcher(str).matches();
	}

	private static String getInvokeType(String str) {

		if (INVOKE_INIT_PATTERN.matcher(str).matches()) {// 构造函数
			return "invoke_init";
		}
		if (INVOKE_STATIC_PATTERN.matcher(str).matches()) {
			return "invoke_static";
		}
		if (INVOKE_MEMBER_PATTERN.matcher(str).matches()) {
			return "invoke_member";
		}
		return "unknown";
	}

	public static String getInitMethod(String str) {
		return str.substring(0, str.indexOf("("));
	}

}
