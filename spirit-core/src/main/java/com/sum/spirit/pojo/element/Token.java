package com.sum.spirit.pojo.element;

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

	public Token copy() {
		Token token = null;
		if (canSplit()) {
			token = new Token(type, ((Statement) getValue()).copy());
		} else {
			token = new Token(type, value);
		}
		token.copyAttributes(this);
		return token;
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
