package com.sum.spirit.pojo.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The enum of keyword
 */
public enum KeywordEnum {
	PACKAGE("package", Type.STRUCT), //
	IMPORT("import", Type.STRUCT), //
	INTERFACE("interface", Type.STRUCT), //
	ABSTRACT("abstract", Type.STRUCT), //
	CLASS("class", Type.STRUCT), //
	FUNC("func", Type.STRUCT), //
	RETURN("return", Type.LINE), //
	IF("if", Type.LINE), //
	DO("do", Type.LINE), //
	WHILE("while", Type.LINE), //
	CONTINUE("continue", Type.LINE), //
	BREAK("break", Type.LINE), //
	TRY("try", Type.LINE), //
	THROW("throw", Type.LINE), //
	SYNC("sync", Type.LINE), //
	PRINT("print", Type.LINE), //
	DEBUG("debug", Type.LINE), //
	ERROR("error", Type.LINE), //
	EXTENDS("extends", Type.TOKEN), //
	IMPLS("impls", Type.TOKEN), //
	THROWS("throws", Type.TOKEN), //
	SUPER("super", Type.TOKEN), //
	THIS("this", Type.TOKEN), //
	ELSE("else", Type.TOKEN), //
	FOR("for", Type.TOKEN), //
	IN("in", Type.TOKEN), //
	CATCH("catch", Type.TOKEN), //
	FINALLY("finally", Type.TOKEN), //
	INSTANCEOF("instanceof", Type.TOKEN), //
	STATIC("static", Type.MODIFIER), //
	PUBLIC("public", Type.MODIFIER), //
	PRIVATE("private", Type.MODIFIER), //
	PROTECTED("protected", Type.MODIFIER), //
	CONST("const", Type.MODIFIER), //
	VOLATILE("volatile", Type.MODIFIER), //
	SYNCH("synch", Type.MODIFIER);//

	public static final Map<String, KeywordEnum> KEYWORD_MAP = new LinkedHashMap<>();

	static {
		for (KeywordEnum keywordEnum : values())
			KEYWORD_MAP.put(keywordEnum.value, keywordEnum);
	}

	public static boolean isKeyword(String value) {
		return KEYWORD_MAP.containsKey(value);
	}

	public static KeywordEnum getKeyword(String value) {
		return KEYWORD_MAP.get(value);
	}

	public static boolean isStruct(String value) {
		return isKeyword(value) && getKeyword(value).type == Type.STRUCT;
	}

	public static boolean isLine(String value) {
		return isKeyword(value) && getKeyword(value).type == Type.LINE;
	}

	public static boolean isModifier(String value) {
		return isKeyword(value) && getKeyword(value).type == Type.MODIFIER;
	}

	public String value;

	public Type type;

	private KeywordEnum(String value, Type type) {
		this.value = value;
		this.type = type;
	}

	public enum Type {
		STRUCT, LINE, TOKEN, MODIFIER
	}

}
