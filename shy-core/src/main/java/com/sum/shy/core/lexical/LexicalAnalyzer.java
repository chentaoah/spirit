package com.sum.shy.core.lexical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.sum.shy.utils.LineUtils;

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

	public static final String[] REGEX_SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "[|]{2}", "<<", "\\!",
			"=", "\\+", "-", "\\*", "/", "%", "<", ">", "\\[", "\\]", "\\{", "\\}", "\\(", "\\)", "\\:", ",", ";",
			"\\?" };

	public static final String[] SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "||", "<<", "!", "=", "+", "-",
			"*", "/", "%", "<", ">", "[", "]", "{", "}", "(", ")", ":", ",", ";", "?" };

	public static final String[] BAD_SYMBOLS = new String[] { "= =", "! =", "< =", "> =", "\\+ \\+", "- -", "< <" };

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+.[A-Z]+\\w+$");

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
			// 如果是接续字符,则记录起始位置
			if (start < 0 && isContinueChar(c))
				start = i;
			// .访问符会及时更新
			if (c == '.')
				start = i;

			if (c == '"') {// 字符串
				LineUtils.replaceString(chars, i, '"', '"', "$str", count++, replacedStrs);

			} else if (c == '[') {// array
				// 如果没有前缀的话
				if (start == -1) {
					LineUtils.replaceString(chars, i, '[', ']', "$array", count++, replacedStrs);
				} else {
					// 不管大小写，都截取
					LineUtils.replaceString(chars, start, '[', ']', "$array_like", count++, replacedStrs);
					i = start;// 索引倒退一些
				}

			} else if (c == '{') {// map
				LineUtils.replaceString(chars, i, '{', '}', "$map", count++, replacedStrs);

			} else if (c == '(') {// 方法调用
				// 如果没有前缀的话
				if (start == -1) {// 子表达式
					LineUtils.replaceString(chars, i, '(', ')', "$subexpress", count++, replacedStrs);
				} else {
					LineUtils.replaceString(chars, start, '(', ')', "$invoke", count++, replacedStrs);
					i = start;// 索引倒退一些
				}

			} else if (c == '<') {// 泛型声明
				if (start >= 0) {
					char e = chars.get(start);
					if (e >= 'A' && e <= 'Z') {// 如果前缀是大写的话,才进行处理
						LineUtils.replaceString(chars, start, '<', '>', '(', ')', "$generic", count++, replacedStrs);
						i = start;
					}
				}
			}
			// 如果不是接续字符,则重置起始位置
			if (!isContinueChar(c))
				start = -1;

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

		// 6.如果包含.但是又不是数字的话，则再拆一次
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			if (word.indexOf(".") > 0 && !TYPE_END_PATTERN.matcher(word).matches()
					&& !SemanticDelegate.isDouble(word)) {
				List<String> subWords = new ArrayList<>(Arrays.asList(word.replaceAll("\\.", " .").split(" ")));
				words.remove(i);
				words.addAll(i, subWords);
			}
		}

		// 7.重新将替换的字符串替换回来
		for (int i = 0; i < words.size(); i++) {
			String str = replacedStrs.get(words.get(i));
			if (str != null) {
				words.set(i, str);
			}
		}

		return words;

	}

	/**
	 * 是否接续字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isContinueChar(char c) {
		return c == '@' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_'
				|| c == '.';
	}

	private static List<Character> getChars(String text) {
		List<Character> list = new LinkedList<>();
		for (char c : text.toCharArray())
			list.add(c);
		return list;
	}

}
