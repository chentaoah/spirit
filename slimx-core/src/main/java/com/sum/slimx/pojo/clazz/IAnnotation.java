package com.sum.slimx.pojo.clazz;

import com.sum.slimx.pojo.element.Element;
import com.sum.slimx.pojo.element.Token;

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
