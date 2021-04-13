package com.sum.spirit.core.clazz.frame;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.core.element.entity.Token;

public abstract class TokenEntity extends TypeEntity {

	public Token token;

	public TokenEntity(Token token) {
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
