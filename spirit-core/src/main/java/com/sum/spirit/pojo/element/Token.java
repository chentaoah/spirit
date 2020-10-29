package com.sum.spirit.pojo.element;

import java.util.Map;

import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

public class Token extends Semantic {

	public Object value;

	public Token() {
		super(null);
	}

	public Token(TokenTypeEnum tokenType, Object value) {
		super(tokenType);
		this.value = value;
	}

	public Token(TokenTypeEnum tokenType, Object value, Map<AttributeEnum, Object> attributes) {
		super(tokenType);
		this.value = value;
		this.attributes = attributes;
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
