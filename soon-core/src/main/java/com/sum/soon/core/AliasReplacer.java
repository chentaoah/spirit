package com.sum.soon.core;

import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.Import;
import com.sum.soon.utils.LineUtils;

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
			// 确保字符不在字符串中
			if (c == '"' && LineUtils.isNotEscaped(code, i))
				flag = !flag;
			// 是一个普通字符，并且首字母和别名首字母相同
			if (!flag && LineUtils.isLetter(c) && c == alias.charAt(0)) {
				// 前面的字符不是一个字母
				if (i - 1 >= 0 && LineUtils.isLetter(code.charAt(i - 1)))
					continue;
				// 从匹配的字符开始，截取出和别名长度一致的字符串
				String str = code.substring(i, i + alias.length());
				// 判断是否一致
				if (alias.equals(str)) {
					// 后面的字符不是一个字母
					if (i + alias.length() < code.length() && LineUtils.isLetter(code.charAt(i + alias.length())))
						continue;
					// 替换
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
