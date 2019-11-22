package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
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
	public boolean isFinal() {
		return token.isType();
	}

	@Override
	public String getTypeName() {
		if (token.isType()) {
			String typeName = (String) token.value;
			if (typeName != null) {
				if (typeName.contains("<") && typeName.contains(">")) {
					return typeName.substring(0, typeName.indexOf("<"));

				} else if (typeName.contains("[") && typeName.contains("]")) {
					return typeName.substring(0, typeName.indexOf("["));

				} else {
					return typeName;
				}
			}
		}
		return null;
	}

	@Override
	public List<String> getGenericTypes() {
		if (token.isType()) {
			String typeName = (String) token.value;
			if (typeName != null) {
				if (typeName.contains("<") && typeName.contains(">")) {
					List<String> list = Splitter.on(CharMatcher.anyOf("<,>")).omitEmptyStrings().trimResults()
							.splitToList(typeName);
					list.remove(0);
					return list;
				}
			}
		}
		return new ArrayList<>();
	}

	@Override
	public String toString() {
		return token.isType() ? token.value.toString() : token.toString();
	}

}
