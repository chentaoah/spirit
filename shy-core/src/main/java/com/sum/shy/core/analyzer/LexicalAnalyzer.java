package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
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

	public static final char[] CHAR_SYMBOLS = new char[] { '=', '+', '-', '*', '/', '%', '<', '>', '&', '|', '!', '(',
			')', '[', ']', '{', '}', ':', ',' };

	public static final String[] REGEX_SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "[|]{2}", "\\!", "=",
			"\\+", "-", "\\*", "/", "%", "<", ">", "\\[", "\\]", "\\{", "\\}", "\\(", "\\)", "\\:", "," };

	public static final String[] SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "!", "=", "+", "-", "*",
			"/", "%", "<", ">", "[", "]", "{", "}", "(", ")", ":", "," };

	public static final String[] BAD_SYMBOLS = new String[] { "= =", "! =", "\\+ \\+", "- -" };

	public static List<String> getWords(String text) {

		// 防止空字符串
		if (text == null || text.length() == 0) {
			return new ArrayList<>();
		}

		// 拆分的单元
		List<String> words = new ArrayList<>();
		// 替换的字符串
		Map<String, String> replacedStrs = new HashMap<>();

		text = text.trim();
		// 将text字符化
		List<Character> chars = getChars(text);

		// 1.整体替换
		for (int i = 0, count = 0, start = -1; i < chars.size(); i++) {// i为游标
			char c = chars.get(i);
			// 如果是字符,则记下该位置
			if (start < 0) {
				if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '.') {
					start = i;
				}
			}
			if (c == '"') {// 字符串
				LineUtils.replaceString(chars, i, '"', '"', "$str", count++, replacedStrs);

			} else if (c == '[') {// array
				// 如果没有前缀的话
				if (start == -1) {
					LineUtils.replaceString(chars, i, '[', ']', "$array", count++, replacedStrs);
				} else {
					// 不管大小写，都截取
					LineUtils.replaceString(chars, start, '[', ']', "$declare", count++, replacedStrs);
					i = start;// 索引倒退一些
				}

			} else if (c == '{') {// map
				LineUtils.replaceString(chars, i, '{', '}', "$map", count++, replacedStrs);

			} else if (c == '(') {// 方法调用
				// 为了支持java原生for循环,这里见到括号不认为是一个方法调用
				if (!"for".equals(text.substring(start, i))) {
					LineUtils.replaceString(chars, start, '(', ')', "$invoke", count++, replacedStrs);
					i = start;// 索引倒退一些
				}

			} else if (c == '<') {// 泛型声明
				char e = chars.get(start);
				if (e >= 'A' && e <= 'Z') {// 如果前缀是大写的话,才进行处理
					LineUtils.replaceString(chars, start, '<', '>', '(', ')', "$generic", count++, replacedStrs);
					i = start;
				} else if ("map".equals(text.substring(start, i))) {// 如果是map类型也进行替换
					LineUtils.replaceString(chars, start, '<', '>', '(', ')', "$generic", count++, replacedStrs);
					i = start;
				}

			}
			// 如果是其他东西的话,则结束标记
			if (c == ' ' || isSymbols(c)) {
				start = -1;
			}

		}

		text = Joiner.on("").join(chars);

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

	private static List<Character> getChars(String text) {
		List<Character> list = new LinkedList<>();
		for (char c : text.toCharArray()) {
			list.add(c);
		}
		return list;
	}

	private static boolean isSymbols(char c) {
		for (char cs : CHAR_SYMBOLS) {
			if (c == cs) {
				return true;
			}
		}
		return false;
	}

}
