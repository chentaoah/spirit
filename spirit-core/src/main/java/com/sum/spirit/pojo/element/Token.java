package com.sum.spirit.pojo.element;

import java.util.Map;

import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

public class Token extends AttributeMap {

	public TokenTypeEnum type;

	public Object value;

	public Token() {
	}

	public Token(TokenTypeEnum type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Token(TokenTypeEnum type, Object value, Map<AttributeEnum, Object> attributes) {
		this.type = type;
		this.value = value;
		this.attributes = attributes;
	}

	@Override
	public TokenTypeEnum getType() {
		return type;
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
		return "<" + type + ", " + value + ">";
	}

}
