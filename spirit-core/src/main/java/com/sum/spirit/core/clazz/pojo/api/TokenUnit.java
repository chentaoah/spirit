package com.sum.spirit.core.clazz.pojo.api;

import com.sum.spirit.core.common.enums.AttributeEnum;
import com.sum.spirit.core.element.pojo.Token;

public abstract class TokenUnit extends TypeUnit {

	public Token token;

	public TokenUnit(Token token) {
		this.token = token;
	}

	public String getName() {
		if (token.isAnnotation()) {
			return token.attr(AttributeEnum.SIMPLE_NAME);

		} else if (token.isVariable()) {
			return token.toString();
		}
		throw new RuntimeException("Unsupported semantics!semantics:" + token.tokenType);
	}

	@Override
	public String toString() {
		return token.toString();
	}

}
