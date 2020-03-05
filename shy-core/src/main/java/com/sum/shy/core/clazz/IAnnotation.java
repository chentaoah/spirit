package com.sum.shy.core.clazz;

import com.sum.shy.core.document.Element;
import com.sum.shy.core.document.Token;

public class IAnnotation {

	public Token token;

	public IAnnotation(Element element) {
		this(element.getToken(0));
	}

	public IAnnotation(Token token) {
		this.token = token;
	}

}
