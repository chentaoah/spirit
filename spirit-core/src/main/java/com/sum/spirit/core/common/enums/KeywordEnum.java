package com.sum.spirit.core.common.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum KeywordEnum {

	PACKAGE("package", KeywordTypeEnum.STRUCT), //
	IMPORT("import", KeywordTypeEnum.STRUCT), //
	INTERFACE("interface", KeywordTypeEnum.STRUCT), //
	ABSTRACT("abstract", KeywordTypeEnum.STRUCT), //
	CLASS("class", KeywordTypeEnum.STRUCT), //
	FUNC("func", KeywordTypeEnum.STRUCT), //

	RETURN("return", KeywordTypeEnum.LINE), //
	IF("if", KeywordTypeEnum.LINE), //
	DO("do", KeywordTypeEnum.LINE), //
	WHILE("while", KeywordTypeEnum.LINE), //
	CONTINUE("continue", KeywordTypeEnum.LINE), //
	BREAK("break", KeywordTypeEnum.LINE), //
	TRY("try", KeywordTypeEnum.LINE), //
	THROW("throw", KeywordTypeEnum.LINE), //
	SYNC("sync", KeywordTypeEnum.LINE), //
	PRINT("print", KeywordTypeEnum.LINE), //
	DEBUG("debug", KeywordTypeEnum.LINE), //
	ERROR("error", KeywordTypeEnum.LINE), //

	EXTENDS("extends", KeywordTypeEnum.TOKEN), //
	IMPLS("impls", KeywordTypeEnum.TOKEN), //
	THROWS("throws", KeywordTypeEnum.TOKEN), //
	SUPER("super", KeywordTypeEnum.TOKEN), //
	THIS("this", KeywordTypeEnum.TOKEN), //
	ELSE("else", KeywordTypeEnum.TOKEN), //
	FOR("for", KeywordTypeEnum.TOKEN), //
	IN("in", KeywordTypeEnum.TOKEN), //
	CATCH("catch", KeywordTypeEnum.TOKEN), //
	FINALLY("finally", KeywordTypeEnum.TOKEN), //
	INSTANCEOF("instanceof", KeywordTypeEnum.TOKEN), //

	STATIC("static", KeywordTypeEnum.MODIFIER), //
	PUBLIC("public", KeywordTypeEnum.MODIFIER), //
	PRIVATE("private", KeywordTypeEnum.MODIFIER), //
	PROTECTED("protected", KeywordTypeEnum.MODIFIER), //
	CONST("const", KeywordTypeEnum.MODIFIER), //
	VOLATILE("volatile", KeywordTypeEnum.MODIFIER), //
	SYNCH("synch", KeywordTypeEnum.MODIFIER); //

	public static final Map<String, KeywordEnum> KEYWORD_MAP = new LinkedHashMap<>();

	static {
		for (KeywordEnum keywordEnum : values()) {
			KEYWORD_MAP.put(keywordEnum.value, keywordEnum);
		}
	}

	public static boolean isKeyword(String value) {
		return KEYWORD_MAP.containsKey(value);
	}

	public static KeywordEnum getKeyword(String value) {
		return KEYWORD_MAP.get(value);
	}

	public static boolean isStruct(String value) {
		return isKeyword(value) && getKeyword(value).type == KeywordTypeEnum.STRUCT;
	}

	public static boolean isLine(String value) {
		return isKeyword(value) && getKeyword(value).type == KeywordTypeEnum.LINE;
	}

	public static boolean isModifier(String value) {
		return isKeyword(value) && getKeyword(value).type == KeywordTypeEnum.MODIFIER;
	}

	public static boolean isKeywordVariable(String value) {
		return KeywordEnum.SUPER.value.equals(value) || KeywordEnum.THIS.value.equals(value);
	}

	public static boolean isSuper(String value) {
		return KeywordEnum.SUPER.value.equals(value);
	}

	public static boolean isThis(String value) {
		return KeywordEnum.THIS.value.equals(value);
	}

	public String value;

	public KeywordTypeEnum type;

	private KeywordEnum(String value, KeywordTypeEnum type) {
		this.value = value;
		this.type = type;
	}

	public enum KeywordTypeEnum {
		STRUCT, LINE, TOKEN, MODIFIER
	}

}
