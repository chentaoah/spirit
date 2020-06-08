package com.sum.shy.clazz.pojo;

import com.sum.shy.document.pojo.Element;
import com.sum.shy.document.pojo.Token;

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
