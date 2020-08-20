package com.sum.spirit.pojo.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeywordTable {

	public static final Map<String, Keyword> KEYWORDS = new LinkedHashMap<>();

	static {
		KEYWORDS.put("package", new Keyword("package", Keyword.STRUCT));
		KEYWORDS.put("import", new Keyword("import", Keyword.STRUCT));
		KEYWORDS.put("interface", new Keyword("interface", Keyword.STRUCT));
		KEYWORDS.put("abstract", new Keyword("abstract", Keyword.STRUCT));
		KEYWORDS.put("class", new Keyword("class", Keyword.STRUCT));
		KEYWORDS.put("func", new Keyword("func", Keyword.STRUCT));

		KEYWORDS.put("if", new Keyword("if", Keyword.LINE));
		KEYWORDS.put("do", new Keyword("do", Keyword.LINE));
		KEYWORDS.put("while", new Keyword("while", Keyword.LINE));
		KEYWORDS.put("try", new Keyword("try", Keyword.LINE));
		KEYWORDS.put("sync", new Keyword("sync", Keyword.LINE));
		KEYWORDS.put("return", new Keyword("return", Keyword.LINE));
		KEYWORDS.put("continue", new Keyword("continue", Keyword.LINE));
		KEYWORDS.put("break", new Keyword("break", Keyword.LINE));
		KEYWORDS.put("throw", new Keyword("throw", Keyword.LINE));
		KEYWORDS.put("print", new Keyword("print", Keyword.LINE));
		KEYWORDS.put("debug", new Keyword("debug", Keyword.LINE));
		KEYWORDS.put("error", new Keyword("error", Keyword.LINE));

		KEYWORDS.put("extends", new Keyword("extends", Keyword.TOKEN));
		KEYWORDS.put("impls", new Keyword("impls", Keyword.TOKEN));
		KEYWORDS.put("throws", new Keyword("throws", Keyword.TOKEN));
		KEYWORDS.put("else", new Keyword("else", Keyword.TOKEN));
		KEYWORDS.put("for", new Keyword("for", Keyword.TOKEN));
		KEYWORDS.put("in", new Keyword("in", Keyword.TOKEN));
		KEYWORDS.put("catch", new Keyword("catch", Keyword.TOKEN));
		KEYWORDS.put("finally", new Keyword("finally", Keyword.TOKEN));
		KEYWORDS.put("instanceof", new Keyword("instanceof", Keyword.TOKEN));
	}

	public static boolean isKeyword(String value) {
		return KEYWORDS.containsKey(value);
	}

	public static Keyword getKeyword(String value) {
		return KEYWORDS.get(value);
	}

	public static boolean isStruct(String value) {
		return isKeyword(value) && getKeyword(value).isStruct();
	}

	public static boolean isLine(String value) {
		return isKeyword(value) && getKeyword(value).isLine();
	}

}
