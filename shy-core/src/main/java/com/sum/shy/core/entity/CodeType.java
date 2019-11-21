package com.sum.shy.core.entity;

import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.api.Type;

public class CodeType implements Type {

	public Token token;

	public CodeType(Token token) {
		this.token = token;
	}

	public CodeType(String type) {
		this.token = SemanticDelegate.getToken(type);
	}

	@Override
	public boolean isAccurate() {
		return token.isType();
	}

	@Override
	public String getTypeName() {
		return token.isType() ? token.value.toString() : null;
	}

	@Override
	public String toString() {
		return token.isType() ? token.value.toString() : token.toString();
	}

}
