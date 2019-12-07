package com.sum.shy.core.analyzer;

import java.util.Map;

import com.sum.shy.core.entity.CtClass;

public class AliasReplacer {

	public static String replace(CtClass clazz, String text) {
		for (Map.Entry<String, String> entry : clazz.importAliases.entrySet()) {
			text = text.replaceAll(String.format("(?!((?<=\").*?(?=\")))\\b%s\\b", entry.getKey()), entry.getValue());
		}
		return text;
	}

}
