package com.sum.shy.core.lexical;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.entity.Symbol;

public class SymbolTable {

	public static Map<String, Symbol> symbols = new LinkedHashMap<>();

	static {

		// 运算符
		symbols.put("++", new Symbol(Symbol.OPERATOR, "\\+\\+", "\\+ \\+", "++", 40, Symbol.MULTIPLE));
		symbols.put("--", new Symbol(Symbol.OPERATOR, "--", "- -", "--", 40, Symbol.MULTIPLE));
		symbols.put("!", new Symbol(Symbol.OPERATOR, "\\!", "!", 40, Symbol.RIGHT));
		symbols.put("*", new Symbol(Symbol.OPERATOR, "\\*", "*", 35, Symbol.DOUBLE));
		symbols.put("/", new Symbol(Symbol.OPERATOR, "/", "/", 35, Symbol.DOUBLE));
		symbols.put("%", new Symbol(Symbol.OPERATOR, "%", "%", 35, Symbol.DOUBLE));
		symbols.put("+", new Symbol(Symbol.OPERATOR, "\\+", "+", 30, Symbol.DOUBLE));
		symbols.put("-", new Symbol(Symbol.OPERATOR, "-", "-", 30, Symbol.MULTIPLE));
		symbols.put("<<", new Symbol(Symbol.OPERATOR, "<<", "< <", "<<", 25, Symbol.DOUBLE));
		symbols.put(">>", new Symbol(Symbol.OPERATOR, ">>", "> >", ">>", 25, Symbol.DOUBLE));
		symbols.put("&", new Symbol(Symbol.OPERATOR, "&", "&", 20, Symbol.DOUBLE));
		symbols.put("^", new Symbol(Symbol.OPERATOR, "\\^", "^", 20, Symbol.DOUBLE));
		symbols.put("|", new Symbol(Symbol.OPERATOR, "[|]{1}", "|", 20, Symbol.DOUBLE));
		symbols.put("==", new Symbol(Symbol.OPERATOR, "==", "= =", "==", 15, Symbol.DOUBLE));
		symbols.put("!=", new Symbol(Symbol.OPERATOR, "!=", "! =", "!=", 15, Symbol.DOUBLE));
		symbols.put("<=", new Symbol(Symbol.OPERATOR, "<=", "< =", "<=", 15, Symbol.DOUBLE));
		symbols.put(">=", new Symbol(Symbol.OPERATOR, ">=", "> =", ">=", 15, Symbol.DOUBLE));
		symbols.put("<", new Symbol(Symbol.OPERATOR, "<", "<", 15, Symbol.DOUBLE));
		symbols.put(">", new Symbol(Symbol.OPERATOR, ">", ">", 15, Symbol.DOUBLE));
		symbols.put("&&", new Symbol(Symbol.OPERATOR, "&&", "& &", "&&", 10, Symbol.DOUBLE));
		symbols.put("||", new Symbol(Symbol.OPERATOR, "[|]{2}", "\\| \\|", "||", 10, Symbol.DOUBLE));
		symbols.put("?", new Symbol(Symbol.OPERATOR, "\\?", "?", 5, Symbol.DOUBLE));
		symbols.put("=", new Symbol(Symbol.OPERATOR, "=", "=", 5, Symbol.DOUBLE));
		// 分隔符
		symbols.put("[", new Symbol(Symbol.SEPARATOR, "\\[", "[", 0, Symbol.NONE));
		symbols.put("]", new Symbol(Symbol.SEPARATOR, "\\]", "]", 0, Symbol.NONE));
		symbols.put("{", new Symbol(Symbol.SEPARATOR, "\\{", "{", 0, Symbol.NONE));
		symbols.put("}", new Symbol(Symbol.SEPARATOR, "\\}", "}", 0, Symbol.NONE));
		symbols.put("(", new Symbol(Symbol.SEPARATOR, "\\(", "(", 0, Symbol.NONE));
		symbols.put(")", new Symbol(Symbol.SEPARATOR, "\\)", ")", 0, Symbol.NONE));
		symbols.put(":", new Symbol(Symbol.SEPARATOR, "\\:", ":", 0, Symbol.NONE));
		symbols.put("::", new Symbol(Symbol.SEPARATOR, "[:]{2}", "\\: \\:", "::", 0, Symbol.NONE));
		symbols.put(",", new Symbol(Symbol.SEPARATOR, ",", ",", 0, Symbol.NONE));
		symbols.put(";", new Symbol(Symbol.SEPARATOR, ";", ";", 0, Symbol.NONE));

	}

	public static List<Symbol> selectSingleSymbols() {
		List<Symbol> list = new ArrayList<>();
		for (Symbol symbol : symbols.values()) {
			if (symbol.value.length() == 1)
				list.add(symbol);
		}
		return list;
	}

	public static List<Symbol> selectDoubleSymbols() {
		List<Symbol> list = new ArrayList<>();
		for (Symbol symbol : symbols.values()) {
			if (symbol.value.length() == 2)
				list.add(symbol);
		}
		return list;
	}

}
