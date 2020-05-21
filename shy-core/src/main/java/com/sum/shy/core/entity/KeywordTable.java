package com.sum.shy.core.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeywordTable {

	public static Map<String, Keyword> keywords = new LinkedHashMap<>();

	static {
		keywords.put("package", new Keyword(Keyword.STRUCT, "package"));
		keywords.put("import", new Keyword(Keyword.STRUCT, "import"));
		keywords.put("interface", new Keyword(Keyword.STRUCT, "interface"));
		keywords.put("abstract", new Keyword(Keyword.STRUCT, "abstract"));
		keywords.put("class", new Keyword(Keyword.STRUCT, "class"));
		keywords.put("func", new Keyword(Keyword.STRUCT, "func"));

		keywords.put("if", new Keyword(Keyword.LINE, "if"));
		keywords.put("do", new Keyword(Keyword.LINE, "do"));
		keywords.put("while", new Keyword(Keyword.LINE, "while"));
		keywords.put("try", new Keyword(Keyword.LINE, "try"));
		keywords.put("sync", new Keyword(Keyword.LINE, "sync"));
		keywords.put("return", new Keyword(Keyword.LINE, "return"));
		keywords.put("continue", new Keyword(Keyword.LINE, "continue"));
		keywords.put("break", new Keyword(Keyword.LINE, "break"));
		keywords.put("throw", new Keyword(Keyword.LINE, "throw"));
		keywords.put("print", new Keyword(Keyword.LINE, "print"));
		keywords.put("debug", new Keyword(Keyword.LINE, "debug"));
		keywords.put("error", new Keyword(Keyword.LINE, "error"));

		keywords.put("extends", new Keyword(Keyword.TOKEN, "extends"));
		keywords.put("impl", new Keyword(Keyword.TOKEN, "impl"));
		keywords.put("throws", new Keyword(Keyword.TOKEN, "throws"));
		keywords.put("else", new Keyword(Keyword.TOKEN, "else"));
		keywords.put("for", new Keyword(Keyword.TOKEN, "for"));
		keywords.put("in", new Keyword(Keyword.TOKEN, "in"));
		keywords.put("catch", new Keyword(Keyword.TOKEN, "catch"));
		keywords.put("finally", new Keyword(Keyword.TOKEN, "finally"));
		keywords.put("instanceof", new Keyword(Keyword.TOKEN, "instanceof"));
	}

	public static boolean isKeyword(String value) {
		return keywords.containsKey(value);
	}

	public static Keyword getKeyword(String value) {
		return keywords.get(value);
	}

	public static boolean isStructKeyword(String value) {
		return isKeyword(value) && getKeyword(value).isStruct();
	}

	public static boolean isLineKeyword(String value) {
		return isKeyword(value) && getKeyword(value).isLine();
	}

}
