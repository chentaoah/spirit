package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.utils.LineUtils;

/**
 * 词法分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class LexicalAnalyzer {

	public static final String[] REGEX_SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "[|]{2}", "\\!", "=",
			"\\+", "-", "\\*", "/", "%", "<", ">", "\\[", "\\]", "\\{", "\\}", "\\(", "\\)", "\\:", "," };

	public static final String[] SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "!", "=", "+", "-", "*",
			"/", "%", "<", ">", "[", "]", "{", "}", "(", ")", ":", "," };

	public static final String[] BAD_SYMBOLS = new String[] { "= =", "! =" };

	/**
	 * 将语句拆分成一个一个单元
	 * 
	 * @param text
	 * @return
	 */
	public static List<String> getWords(String text) {

		// 防止空字符串
		if (text == null || text.length() == 0) {
			return new ArrayList<>();
		}
		// 拆分的单元
		List<String> words = new ArrayList<>();
		// 替换的字符串
		Map<String, String> replacedStrs = new HashMap<>();

		// 去掉前后的空格
		text = text.trim();

		// 1.将字符串,方法调用,数组,键值对,都当做一个整体来对待
		// 这里需要解决一个括号谁套谁的问题
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '"') {
				text = LineUtils.replaceString(text, '"', '"', "str", count++, replacedStrs);
			} else if (text.charAt(i) == '(') {
				text = LineUtils.replaceString(text, '(', ')', "invoke", count++, replacedStrs, true);
			} else if (text.charAt(i) == '[') {
				text = LineUtils.replaceString(text, '[', ']', "array", count++, replacedStrs);
			} else if (text.charAt(i) == '{') {
				text = LineUtils.replaceString(text, '{', '}', "map", count++, replacedStrs);
			}
		}

		// 2.处理操作符,添加空格,方便后面的拆分
		for (int i = 0; i < REGEX_SYMBOLS.length; i++) {
			text = text.replaceAll(REGEX_SYMBOLS[i], " " + SYMBOLS[i] + " ");
		}

		// 3.将多余的空格去掉
		text = LineUtils.removeSpace(text);

		// 4.将那些被分离的符号,紧贴在一起
		for (String str : BAD_SYMBOLS) {
			text = text.replaceAll(str, str.replaceAll(" ", ""));
		}

		// 5.根据操作符,进行拆分
		words = new ArrayList<>(Arrays.asList(text.split(" ")));

		// 6.重新将替换的字符串替换回来
		for (int i = 0; i < words.size(); i++) {
			String str = replacedStrs.get(words.get(i));
			if (str != null) {
				words.set(i, str);
			}
		}

		return words;

	}

}
