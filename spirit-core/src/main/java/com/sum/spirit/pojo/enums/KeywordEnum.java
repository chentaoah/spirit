package com.sum.spirit.pojo.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The enum of keyword
 */
public enum KeywordEnum {
	PACKAGE("package", TypeEnum.STRUCT), //
	IMPORT("import", TypeEnum.STRUCT), //
	INTERFACE("interface", TypeEnum.STRUCT), //
	ABSTRACT("abstract", TypeEnum.STRUCT), //
	CLASS("class", TypeEnum.STRUCT), //
	FUNC("func", TypeEnum.STRUCT), //
	RETURN("return", TypeEnum.LINE), //
	IF("if", TypeEnum.LINE), //
	DO("do", TypeEnum.LINE), //
	WHILE("while", TypeEnum.LINE), //
	CONTINUE("continue", TypeEnum.LINE), //
	BREAK("break", TypeEnum.LINE), //
	TRY("try", TypeEnum.LINE), //
	THROW("throw", TypeEnum.LINE), //
	SYNC("sync", TypeEnum.LINE), //
	PRINT("print", TypeEnum.LINE), //
	DEBUG("debug", TypeEnum.LINE), //
	ERROR("error", TypeEnum.LINE), //
	EXTENDS("extends", TypeEnum.TOKEN), //
	IMPLS("impls", TypeEnum.TOKEN), //
	THROWS("throws", TypeEnum.TOKEN), //
	SUPER("super", TypeEnum.TOKEN), //
	THIS("this", TypeEnum.TOKEN), //
	ELSE("else", TypeEnum.TOKEN), //
	FOR("for", TypeEnum.TOKEN), //
	IN("in", TypeEnum.TOKEN), //
	CATCH("catch", TypeEnum.TOKEN), //
	FINALLY("finally", TypeEnum.TOKEN), //
	INSTANCEOF("instanceof", TypeEnum.TOKEN), //
	STATIC("static", TypeEnum.MODIFIER), //
	PUBLIC("public", TypeEnum.MODIFIER), //
	PRIVATE("private", TypeEnum.MODIFIER), //
	PROTECTED("protected", TypeEnum.MODIFIER), //
	CONST("const", TypeEnum.MODIFIER), //
	VOLATILE("volatile", TypeEnum.MODIFIER), //
	SYNCH("synch", TypeEnum.MODIFIER);//

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
		return isKeyword(value) && getKeyword(value).type == TypeEnum.STRUCT;
	}

	public static boolean isLine(String value) {
		return isKeyword(value) && getKeyword(value).type == TypeEnum.LINE;
	}

	public static boolean isModifier(String value) {
		return isKeyword(value) && getKeyword(value).type == TypeEnum.MODIFIER;
	}

	public String value;

	public TypeEnum type;

	private KeywordEnum(String value, TypeEnum type) {
		this.value = value;
		this.type = type;
	}

	public enum TypeEnum {
		STRUCT, LINE, TOKEN, MODIFIER
	}

}
