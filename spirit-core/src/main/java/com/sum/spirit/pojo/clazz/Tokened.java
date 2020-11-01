package com.sum.spirit.pojo.clazz;

import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;

public abstract class Tokened extends Typed {

	public Token token;

	public Tokened(Token token) {
		this.token = token;
	}

	public String getName() {
		if (token.isAnnotation()) {
			return token.getAttribute(AttributeEnum.SIMPLE_NAME);

		} else if (token.isVar()) {
			return token.toString();
		}
		throw new RuntimeException("Unsupported semantics!semantics:" + token.tokenType);
	}

	@Override
	public String toString() {
		return token.toString();
	}

}
