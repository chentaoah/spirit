package com.sum.shy.core.clazz;

import com.sum.shy.core.stmt.Element;
import com.sum.shy.core.stmt.Token;

public class IAnnotation {

	public Token token;

	public IAnnotation(Element element) {
		this(element.getToken(0));
	}

	public IAnnotation(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return token.toString();
	}

}
