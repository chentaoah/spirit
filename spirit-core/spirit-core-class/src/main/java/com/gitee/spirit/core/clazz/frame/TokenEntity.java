package com.gitee.spirit.core.clazz.frame;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.element.entity.Token;

public abstract class TokenEntity extends TypeEntity {

	public Token token;

	public TokenEntity(Token token) {
		this.token = token;
	}

	public String getName() {
		if (token.isAnnotation()) {
			return token.attr(Attribute.SIMPLE_NAME);

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
