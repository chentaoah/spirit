package com.sum.shy.core;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.Import;
import com.sum.shy.core.utils.CharUtils;
import com.sum.shy.core.utils.LineUtils;

public class AliasReplacer {

	public static String replace(IClass clazz, String code) {
		for (Import imp : clazz.imports) {
			if (imp.hasAlias())
				code = replace(code, imp.getAlias(), imp.getClassName());
		}
		return code;
	}

	public static String replace(String code, String alias, String className) {
		boolean flag = false;// 是否进入"符号的范围内
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			if (c == '"' && LineUtils.isBoundary(code, i))
				flag = !flag;
			// 如果不在字符串中，并且是接续字符，首字母相同
			if (!flag && CharUtils.isLetter(c) && c == alias.charAt(0)) {
				// 单词截断的判断
				if (i - 1 >= 0 && CharUtils.isLetter(code.charAt(i - 1)))
					continue;
				String str = code.substring(i, i + alias.length());
				if (alias.equals(str)) {
					// 单词截断的判断
					if (i + alias.length() < code.length() && CharUtils.isLetter(code.charAt(i + alias.length())))
						continue;
					code = new StringBuilder(code).replace(i, i + alias.length(), className).toString();
					i = i + className.length() - 1;
				}
			}
		}
		return code;
	}

	public static void main(String[] args) {
		String text = "G_Aliasxxx=\"Clock moved backwards.G_Alias to generate id for %d milliseconds\"+G_Alias;";
		System.out.println(replace(text, "G_Alias", "XXX"));
	}

}
