package com.sum.shy.core.processor;

import java.util.Map;

import com.sum.shy.clazz.CtClass;

public class AliasReplacer {

	public static String replace(CtClass clazz, String text) {
		for (Map.Entry<String, String> entry : clazz.importAliases.entrySet()) {
			text = text.replaceAll(String.format("(?!((?<=\").*?(?=\")))\\b%s\\b", entry.getKey()), entry.getValue());
		}
		return text;
	}

}
