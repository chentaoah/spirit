package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.enums.TokenEnum;

public class Token extends Attachable {

	public TokenEnum type;

	public Object value;

	public Token() {
	}

	public Token(TokenEnum type, Object value) {
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
		token.copyAtt(this);
		return token;
	}

	@Override
	public TokenEnum getType() {
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
