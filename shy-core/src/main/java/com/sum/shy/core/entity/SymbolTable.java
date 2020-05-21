package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

	public static Map<String, Symbol> symbols = new LinkedHashMap<>();

	static {

		// 运算符
		symbols.put("++",
				new Symbol(Symbol.OPERATOR, "\\+\\+", "\\+ \\+", "++", 40, Symbol.MULTIPLE, Symbol.ARITHMETIC));
		symbols.put("--", new Symbol(Symbol.OPERATOR, "--", "- -", "--", 40, Symbol.MULTIPLE, Symbol.ARITHMETIC));
		symbols.put("!", new Symbol(Symbol.OPERATOR, "\\!", "!", 40, Symbol.RIGHT, Symbol.LOGICAL));
		symbols.put("*", new Symbol(Symbol.OPERATOR, "\\*", "*", 35, Symbol.DOUBLE, Symbol.ARITHMETIC));
		symbols.put("/", new Symbol(Symbol.OPERATOR, "/", "/", 35, Symbol.DOUBLE, Symbol.ARITHMETIC));
		symbols.put("%", new Symbol(Symbol.OPERATOR, "%", "%", 35, Symbol.DOUBLE, Symbol.ARITHMETIC));
		symbols.put("+", new Symbol(Symbol.OPERATOR, "\\+", "+", 30, Symbol.DOUBLE, Symbol.ARITHMETIC));
		symbols.put("-", new Symbol(Symbol.OPERATOR, "-", "-", 30, Symbol.MULTIPLE, Symbol.ARITHMETIC));
		symbols.put("<<", new Symbol(Symbol.OPERATOR, "<<", "< <", "<<", 25, Symbol.DOUBLE, Symbol.BITWISE));
		symbols.put(">>", new Symbol(Symbol.OPERATOR, ">>", "> >", ">>", 25, Symbol.DOUBLE, Symbol.BITWISE));
		symbols.put("&", new Symbol(Symbol.OPERATOR, "&", "&", 20, Symbol.DOUBLE, Symbol.BITWISE));
		symbols.put("^", new Symbol(Symbol.OPERATOR, "\\^", "^", 20, Symbol.DOUBLE, Symbol.BITWISE));
		symbols.put("|", new Symbol(Symbol.OPERATOR, "[|]{1}", "|", 20, Symbol.DOUBLE, Symbol.BITWISE));
		symbols.put("==", new Symbol(Symbol.OPERATOR, "==", "= =", "==", 15, Symbol.DOUBLE, Symbol.RELATION));
		symbols.put("!=", new Symbol(Symbol.OPERATOR, "!=", "! =", "!=", 15, Symbol.DOUBLE, Symbol.RELATION));
		symbols.put("<=", new Symbol(Symbol.OPERATOR, "<=", "< =", "<=", 15, Symbol.DOUBLE, Symbol.RELATION));
		symbols.put(">=", new Symbol(Symbol.OPERATOR, ">=", "> =", ">=", 15, Symbol.DOUBLE, Symbol.RELATION));
		symbols.put("<", new Symbol(Symbol.OPERATOR, "<", "<", 15, Symbol.DOUBLE, Symbol.RELATION));
		symbols.put(">", new Symbol(Symbol.OPERATOR, ">", ">", 15, Symbol.DOUBLE, Symbol.RELATION));
		symbols.put("&&", new Symbol(Symbol.OPERATOR, "&&", "& &", "&&", 10, Symbol.DOUBLE, Symbol.LOGICAL));
		symbols.put("||", new Symbol(Symbol.OPERATOR, "[|]{2}", "\\| \\|", "||", 10, Symbol.DOUBLE, Symbol.LOGICAL));
		symbols.put("?", new Symbol(Symbol.OPERATOR, "\\?", "?", 5, Symbol.DOUBLE, Symbol.CONDITIONAL));
		symbols.put("=", new Symbol(Symbol.OPERATOR, "=", "=", 5, Symbol.DOUBLE, Symbol.ASSIGN));
		// 分隔符
		symbols.put("[", new Symbol(Symbol.SEPARATOR, "\\[", "[", 0, Symbol.NONE, Symbol.NONE));
		symbols.put("]", new Symbol(Symbol.SEPARATOR, "\\]", "]", 0, Symbol.NONE, Symbol.NONE));
		symbols.put("{", new Symbol(Symbol.SEPARATOR, "\\{", "{", 0, Symbol.NONE, Symbol.NONE));
		symbols.put("}", new Symbol(Symbol.SEPARATOR, "\\}", "}", 0, Symbol.NONE, Symbol.NONE));
		symbols.put("(", new Symbol(Symbol.SEPARATOR, "\\(", "(", 0, Symbol.NONE, Symbol.NONE));
		symbols.put(")", new Symbol(Symbol.SEPARATOR, "\\)", ")", 0, Symbol.NONE, Symbol.NONE));
		symbols.put(":", new Symbol(Symbol.SEPARATOR, "\\:", ":", 0, Symbol.NONE, Symbol.NONE));
		symbols.put("::", new Symbol(Symbol.SEPARATOR, "[:]{2}", "\\: \\:", "::", 0, Symbol.NONE, Symbol.NONE));
		symbols.put(",", new Symbol(Symbol.SEPARATOR, ",", ",", 0, Symbol.NONE, Symbol.NONE));
		symbols.put(";", new Symbol(Symbol.SEPARATOR, ";", ";", 0, Symbol.NONE, Symbol.NONE));

	}

	public static List<Symbol> selectSingleSymbols() {// 单字符
		List<Symbol> list = new ArrayList<>();
		for (Symbol symbol : symbols.values()) {
			if (symbol.value.length() == 1)
				list.add(symbol);
		}
		return list;
	}

	public static List<Symbol> selectDoubleSymbols() {// 双字符
		List<Symbol> list = new ArrayList<>();
		for (Symbol symbol : symbols.values()) {
			if (symbol.value.length() == 2)
				list.add(symbol);
		}
		return list;
	}

	public static List<Symbol> selectBinaryOperator() {// 查询二元操作符
		List<Symbol> list = new ArrayList<>();
		for (Symbol symbol : symbols.values()) {
			if (symbol.isDouble())
				list.add(symbol);
		}
		return list;
	}

	public static Symbol selectSymbol(String value) {// 根据值来查找符号
		return symbols.get(value);
	}

	public static int selectPriority(String value) {// 获取优先级
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.priority;
		}
		return -1;
	}

	public static boolean isOperator(String value) {// 是否操作符
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isOperator();
		}
		return false;
	}

	public static boolean isSeparator(String value) {// 是否分隔符
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isSeparator();
		}
		return false;
	}

	public static boolean isArithmetic(String value) {
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isArithmetic();
		}
		return false;
	}

	public static boolean isBitwise(String value) {
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isBitwise();
		}
		return false;
	}

	public static boolean isRelation(String value) {
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isRelation();
		}
		return false;
	}

	public static boolean isLogical(String value) {
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isLogical();
		}
		return false;
	}

	public static boolean isConditional(String value) {
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isConditional();
		}
		return false;
	}

	public static boolean isAssign(String value) {
		if (symbols.containsKey(value)) {
			Symbol symbol = symbols.get(value);
			return symbol.isAssign();
		}
		return false;
	}

}
