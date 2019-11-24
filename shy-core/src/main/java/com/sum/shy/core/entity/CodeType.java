package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.api.Type;

public class CodeType implements Type {

	public CtClass clazz;// 标记在哪个类中被声明

	public Token token;

	public CodeType(CtClass clazz, Token token) {
		this.clazz = clazz;
		this.token = token;
	}

	public CodeType(CtClass clazz, String type) {
		this.clazz = clazz;
		this.token = SemanticDelegate.getToken(type);
	}

	@Override
	public String getClassName() {
		return clazz.findClassName(getTypeName());
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
					// 转换成类全名
					for (int i = 0; i < list.size(); i++) {
						list.set(i, clazz.findClassName(list.get(i)));
					}
					return list;
				}
			}
		}
		return new ArrayList<>();
	}

	@Override
	public boolean isArray() {
		if (token.isType()) {
			String typeName = (String) token.value;
			if (typeName != null && typeName.endsWith("[]")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return token.isType() ? token.value.toString() : token.toString();
	}

}
