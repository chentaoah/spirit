package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.sum.shy.core.analyzer.SemanticDelegate;
import com.sum.shy.core.api.Type;

public class CodeType implements Type {

	public String className;
	public String typeName;
	public List<CodeType> genericTypes = new ArrayList<>();
	public boolean isArray = false;

	public CodeType(CtClass clazz, Token token) {
		resolve(clazz, token);
	}

	public CodeType(CtClass clazz, String type) {
		Token token = SemanticDelegate.getToken(type);
		resolve(clazz, token);
	}

	private void resolve(CtClass clazz, Token token) {
		if (token.isType()) {
			String text = (String) token.value;
			if (text != null) {
				if (text.contains("<") && text.contains(">")) {
					typeName = text.substring(0, text.indexOf("<"));
					// 解析泛型
					List<String> list = Splitter.on(CharMatcher.anyOf("<,>")).omitEmptyStrings().trimResults()
							.splitToList(text);
					list = list.subList(1, list.size());
					for (String type : list) {
						genericTypes.add(new CodeType(clazz, type));
					}
				} else if (text.endsWith("[]")) {// 如果是数组，则typeName为String[]
					typeName = text;
					isArray = true;
				} else {
					typeName = text;
				}
				className = clazz.findClassName(typeName);
			}
		}

	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}

	@Override
	public List<Type> getGenericTypes() {
		List<Type> list = new ArrayList<>();
		list.addAll(genericTypes);
		return list;
	}

	@Override
	public boolean isArray() {
		return isArray;
	}

	@Override
	public String toString() {
		if (!isArray() && genericTypes.size() == 0) {// 普通类型
			return getTypeName();
		} else if (!isArray() && genericTypes.size() > 0) {// 泛型
			return getTypeName() + "<" + Joiner.on(",").join(genericTypes) + ">";
		} else if (isArray() && genericTypes.size() == 0) {// 数组
			return getTypeName();
		}
		return null;
	}

}
