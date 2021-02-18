package com.sum.spirit.core.clazz.frame;

import com.sum.spirit.common.entity.Token;
import com.sum.spirit.common.enums.AttributeEnum;

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
