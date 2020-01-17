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

	public static final String[] REGEX_SYMBOLS = new String[] { "==", "!=", ">=", "<=", "&&", "[|]{2}", "<<", "=",
			"\\!", "\\+", "-", "\\*", "/", "%", "<", ">", "\\[", "\\]", "\\{", "\\}", "\\(", "\\)", "\\:", ",", ";",
			"\\?" };

	public static final String[] SYMBOLS = new String[] { "==", "!=", ">=", "<=", "&&", "||", "<<", "=", "!", "+", "-",
			"*", "/", "%", "<", ">", "[", "]", "{", "}", "(", ")", ":", ",", ";", "?" };

	public static final String[] BAD_SYMBOLS = new String[] { "= =", "! =", "< =", "> =", "\\+ \\+", "- -", "< <" };

	public static final Pattern TYPE_END_PATTERN = Pattern.compile("^[\\s\\S]+.[A-Z]+\\w+$");

	public static List<String> getWords(String text) {

		if (text.trim().equals("if s==\"hello\\\\\" {")) {
			System.out.println();
		}

		if (text == null || text.length() == 0)
			return new ArrayList<>();

		List<String> words = new ArrayList<>();
		Map<String, String> replacedStrs = new HashMap<>();// 被替换的字符串

		text = text.trim();
		List<Character> chars = getChars(text);
		// 1.整体替换
		for (int i = 0, count = 0, start = -1; i < chars.size(); i++) {
			char c = chars.get(i);

			if (start < 0 && isContinueChar(c))// 如果是接续字符,则记录起始位置
				start = i;

			if (c == '.')// .访问符会及时更新
				start = i;

			if (c == '"') {
				replaceWithWhole(chars, i, '"', '"', "$str", count++, replacedStrs);

			} else if (c == '{') {
				replaceWithWhole(chars, i, '{', '}', "$map", count++, replacedStrs);

			} else if (c == '[') {
				replaceWithWhole(chars, start >= 0 ? start : i, '[', ']', "$array_like", count++, replacedStrs);
				i = start >= 0 ? start : i;

			} else if (c == '(') {
				replaceWithWhole(chars, start >= 0 ? start : i, '(', ')', "$invoke_like", count++, replacedStrs);
				i = start >= 0 ? start : i;

			} else if (c == '<') {// 泛型声明
				if (start >= 0) {
					char e = chars.get(start);
					if (e >= 'A' && e <= 'Z') {// 如果首字母是大写的话,才进行处理
						replaceWithWhole(chars, start, '<', '>', '(', ')', "$generic", count++, replacedStrs);
						i = start;
					}
				}
			}

			if (!isContinueChar(c))// 如果不是接续字符,则重置起始位置
				start = -1;

		}

		text = Joiner.on("").join(chars);

		// 2.处理操作符,添加空格,方便后面的拆分
		for (int i = 0; i < REGEX_SYMBOLS.length; i++)
			text = text.replaceAll(REGEX_SYMBOLS[i], " " + SYMBOLS[i] + " ");

		// 3.将多余的空格去掉
		text = LineUtils.removeSpace(text);

		// 4.将那些被分离的符号,紧贴在一起
		for (String str : BAD_SYMBOLS)
			text = text.replaceAll(str, str.replaceAll(" ", ""));

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
			if (str != null)
				words.set(i, str);
		}

		return words;

	}

	public static List<Character> getChars(String text) {
		List<Character> list = new LinkedList<>();
		for (char c : text.toCharArray())
			list.add(c);
		return list;
	}

	public static boolean isContinueChar(char c) {// 是否接续字符
		return c == '@' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_'
				|| c == '.';
	}

	public static void replaceWithWhole(List<Character> chars, int index, char left, char right, String name,
			int number, Map<String, String> replacedStrs) {
		int end = findEnd(chars, index, left, right);
		doReplaceString(chars, index, end, name, number, replacedStrs);
	}

	public static void replaceWithWhole(List<Character> chars, int index, char left, char right, char left1,
			char right1, String name, int number, Map<String, String> replacedStrs) {
		int end = findEnd(chars, index, left, right);
		if (end != -1 && end + 1 < chars.size()) { // 判断后面的符号是否连续
			char c = chars.get(end + 1);
			if (c == left1)
				end = findEnd(chars, end + 1, left1, right1);
		}
		doReplaceString(chars, index, end, name, number, replacedStrs);
	}

	public static int findEnd(List<Character> chars, int index, char left, char right) {
		boolean flag = false;// 是否进入"符号的范围内
		for (int i = index, count = 0; i < chars.size(); i++) {
			char c = chars.get(i);
			if (c == '"' && isBoundary(chars, i)) // 判断是否进入了字符串中
				flag = !flag;
			if (!flag) {
				if (right == '"')// 字符串是比较特殊的,含头不含尾,这里需要特殊处理
					return i;
				if (c == left) {
					count++;
				} else if (c == right) {
					count--;
					if (count == 0)
						return i;
				}
			}
		}
		return -1;

	}

	public static boolean isBoundary(List<Character> chars, int i) {
		int count = 0;
		while (--i >= 0) {
			if (chars.get(i) == '\\') {
				count++;
			} else {
				break;
			}
		}
		return count % 2 == 0;
	}

	public static void doReplaceString(List<Character> chars, int start, int end, String name, int number,
			Map<String, String> replacedStrs) {

		if (end == -1)
			return;

		List<Character> subChars = chars.subList(start, end + 1);// 从字符串里面截取字符串
		replacedStrs.put(name + number, Joiner.on("").join(subChars));

		for (int j = 0; j < end - start + 1; j++)// 删除字符
			chars.remove(start);

		subChars = new ArrayList<>();
		for (char c : (" " + name + number + " ").toCharArray())// 添加字符
			subChars.add(c);

		chars.addAll(start, subChars);
	}

}
