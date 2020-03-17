package com.sum.shy.java;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.Import;

public class AliasReplacer {

	public static String replace(IClass clazz, String text) {
		for (Import iImport : clazz.imports) {
			if (iImport.hasAlias()) {
				String pattern = String.format("(?<!\")(\\b%s\\b)(?!\")", iImport.getAlias());
				text = text.replaceAll(pattern, iImport.getClassName());
			}
		}
		return text;
	}

}
