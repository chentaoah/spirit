package com.gitee.spirit.core.element.entity;

import java.util.HashMap;
import java.util.Map;

import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.core.element.frame.Semantic;

public class Token extends Semantic {

	public Object value;

	public Token() {
		super(null, new HashMap<>());
	}

	public Token(TokenTypeEnum tokenType, Object value) {
		super(tokenType, new HashMap<>());
		this.value = value;
	}

	public Token(TokenTypeEnum tokenType, Object value, Map<String, Object> attributes) {
		super(tokenType, attributes);
		this.value = value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public String debug() {
		return "<" + tokenType + ", " + value + ">";
	}

}
