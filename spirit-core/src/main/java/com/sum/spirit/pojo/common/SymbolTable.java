package com.sum.spirit.pojo.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

	public static final Map<String, Symbol> SYMBOLS = new LinkedHashMap<>();

	public static final List<Symbol> SINGLE_SYMBOLS = new ArrayList<>();

	public static final List<Symbol> DOUBLE_SYMBOLS = new ArrayList<>();

	static {
		// operator
		SYMBOLS.put("++", new Symbol("++", "\\+\\+", "\\+ \\+", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.ARITHMETIC, 40, Symbol.MULTIPLE));
		SYMBOLS.put("--", new Symbol("--", "--", "- -", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.ARITHMETIC, 40, Symbol.MULTIPLE));
		SYMBOLS.put("!", new Symbol("!", "\\!", Symbol.OPERATOR, Symbol.SINGLE, Symbol.LOGICAL, 40, Symbol.RIGHT));
		SYMBOLS.put("*", new Symbol("*", "\\*", Symbol.OPERATOR, Symbol.SINGLE, Symbol.ARITHMETIC, 35, Symbol.BINARY));
		SYMBOLS.put("/", new Symbol("/", "/", Symbol.OPERATOR, Symbol.SINGLE, Symbol.ARITHMETIC, 35, Symbol.BINARY));
		SYMBOLS.put("%", new Symbol("%", "%", Symbol.OPERATOR, Symbol.SINGLE, Symbol.ARITHMETIC, 35, Symbol.BINARY));
		SYMBOLS.put("+", new Symbol("+", "\\+", Symbol.OPERATOR, Symbol.SINGLE, Symbol.ARITHMETIC, 30, Symbol.BINARY));
		SYMBOLS.put("-", new Symbol("-", "-", Symbol.OPERATOR, Symbol.SINGLE, Symbol.ARITHMETIC, 30, Symbol.MULTIPLE));
		SYMBOLS.put("<<", new Symbol("<<", "<<", "< <", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.BITWISE, 25, Symbol.BINARY));
		SYMBOLS.put(">>", new Symbol(">>", ">>", "> >", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.BITWISE, 25, Symbol.BINARY));
		SYMBOLS.put("&", new Symbol("&", "&", Symbol.OPERATOR, Symbol.SINGLE, Symbol.BITWISE, 20, Symbol.BINARY));
		SYMBOLS.put("^", new Symbol("^", "\\^", Symbol.OPERATOR, Symbol.SINGLE, Symbol.BITWISE, 20, Symbol.BINARY));
		SYMBOLS.put("|", new Symbol("|", "[|]{1}", Symbol.OPERATOR, Symbol.SINGLE, Symbol.BITWISE, 20, Symbol.BINARY));
		SYMBOLS.put("==", new Symbol("==", "==", "= =", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.RELATION, 15, Symbol.BINARY));
		SYMBOLS.put("!=", new Symbol("!=", "!=", "! =", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.RELATION, 15, Symbol.BINARY));
		SYMBOLS.put("<=", new Symbol("<=", "<=", "< =", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.RELATION, 15, Symbol.BINARY));
		SYMBOLS.put(">=", new Symbol(">=", ">=", "> =", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.RELATION, 15, Symbol.BINARY));
		SYMBOLS.put("<", new Symbol("<", "<", Symbol.OPERATOR, Symbol.SINGLE, Symbol.RELATION, 15, Symbol.BINARY));
		SYMBOLS.put(">", new Symbol(">", ">", Symbol.OPERATOR, Symbol.SINGLE, Symbol.RELATION, 15, Symbol.BINARY));
		SYMBOLS.put("&&", new Symbol("&&", "&&", "& &", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.LOGICAL, 10, Symbol.BINARY));
		SYMBOLS.put("||", new Symbol("||", "[|]{2}", "\\| \\|", Symbol.OPERATOR, Symbol.DOUBLE, Symbol.LOGICAL, 10, Symbol.BINARY));
		SYMBOLS.put("?", new Symbol("?", "\\?", Symbol.OPERATOR, Symbol.SINGLE, Symbol.CONDITIONAL, 5, Symbol.BINARY));
		SYMBOLS.put("=", new Symbol("=", "=", Symbol.OPERATOR, Symbol.SINGLE, Symbol.ASSIGN, 5, Symbol.BINARY));
		// separator
		SYMBOLS.put("[", new Symbol("[", "\\[", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put("]", new Symbol("]", "\\]", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put("{", new Symbol("{", "\\{", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put("}", new Symbol("}", "\\}", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put("(", new Symbol("(", "\\(", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put(")", new Symbol(")", "\\)", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put(":", new Symbol(":", "\\:", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put("::", new Symbol("::", "[:]{2}", "\\: \\:", Symbol.SEPARATOR, Symbol.DOUBLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put(",", new Symbol(",", ",", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));
		SYMBOLS.put(";", new Symbol(";", ";", Symbol.SEPARATOR, Symbol.SINGLE, Symbol.UNKNOWN, 0, Symbol.UNKNOWN));

		for (Symbol symbol : SYMBOLS.values()) {
			if (symbol.isSingle())
				SINGLE_SYMBOLS.add(symbol);
		}

		for (Symbol symbol : SYMBOLS.values()) {
			if (symbol.isDouble())
				DOUBLE_SYMBOLS.add(symbol);
		}

	}

	public static boolean isSymbol(String value) {
		return SYMBOLS.containsKey(value);
	}

	public static Symbol getSymbol(String value) {
		return SYMBOLS.get(value);
	}

	public static int getPriority(String value) {
		if (isSymbol(value))
			return getSymbol(value).priority;
		return -1;
	}

	public static boolean isOperator(String value) {
		if (isSymbol(value))
			return getSymbol(value).isOperator();
		return false;
	}

	public static boolean isSeparator(String value) {
		if (isSymbol(value))
			return getSymbol(value).isSeparator();
		return false;
	}

	public static boolean isArithmetic(String value) {
		if (isSymbol(value))
			return getSymbol(value).isArithmetic();
		return false;
	}

	public static boolean isBitwise(String value) {
		if (isSymbol(value))
			return getSymbol(value).isBitwise();
		return false;
	}

	public static boolean isRelation(String value) {
		if (isSymbol(value))
			return getSymbol(value).isRelation();
		return false;
	}

	public static boolean isLogical(String value) {
		if (isSymbol(value))
			return getSymbol(value).isLogical();
		return false;
	}

	public static boolean isConditional(String value) {
		if (isSymbol(value))
			return getSymbol(value).isConditional();
		return false;
	}

	public static boolean isAssign(String value) {
		if (isSymbol(value))
			return getSymbol(value).isAssign();
		return false;
	}

}
