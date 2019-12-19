package com.sum.shy.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.sum.shy.core.entity.Line;

public class LineUtils {

	// 返回子块
	public static List<Line> getSubLines(List<Line> lines, int index) {
		// 找到子域的结束符"}"
		List<Line> list = new ArrayList<>();
		for (int i = index + 1, count = 1; i < lines.size(); i++) {
			String text = lines.get(i).text;
			// 遍历
			for (int j = 0; j < text.length(); j++) {
				if (text.charAt(j) == '{') {
					count++;
				} else if (text.charAt(j) == '}') {
					count--;
				}
			}
			if (count == 0) {
				break;
			}
			list.add(lines.get(i));
		}
		return list;
	}

	public static List<Line> getAllLines(List<Line> lines, int index) {
		List<Line> list = new ArrayList<>();
		for (int i = index, count = 0; i < lines.size(); i++) {
			String text = lines.get(i).text;
			for (int j = 0; j < text.length(); j++) {
				if (text.charAt(j) == '{') {
					count++;
				} else if (text.charAt(j) == '}') {
					count--;
				}
			}
			list.add(lines.get(i));
			if (count == 0) {
				break;
			}
		}
		return list;
	}

	// 去掉多余的空格
	public static String removeSpace(String line) {
		// 去掉首尾
		line = line.trim();
		while (line.contains("  ")) {
			line = line.replaceAll("  ", " ");
		}
		return line;
	}

	public static String getSpaceByNumber(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String getIndentByNumber(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	public static void replaceString(List<Character> chars, int index, char left, char right, String name, int number,
			Map<String, String> replacedStrs) {

		int end = findEnd(chars, index, left, right);
		replaceString(chars, index, end, name, number, replacedStrs);

	}

	public static void replaceString(List<Character> chars, int index, char left, char right, char left1, char right1,
			String name, int number, Map<String, String> replacedStrs) {

		int end = findEnd(chars, index, left, right);
		// 判断后面的符号是否连续
		if (end != -1 && end + 1 < chars.size()) {
			char c = chars.get(end + 1);
			if (c == left1) {
				end = findEnd(chars, end + 1, left1, right1);
			}
		}
		replaceString(chars, index, end, name, number, replacedStrs);

	}

	private static int findEnd(List<Character> chars, int index, char left, char right) {
		// 是否进入"符号的范围内
		boolean flag = false;
		for (int i = index, count = 0; i < chars.size(); i++) {
			char c = chars.get(i);
			if (c == '"' && chars.get(i - 1 >= 0 ? i - 1 : i) != '\\') {// 判断是否进入了字符串中
				flag = !flag;
			}
			if (!flag) {
				if (right == '"') {// 如果是字符串
					return i;
				}
				if (c == left) {
					count++;
				} else if (c == right) {
					count--;
					if (count == 0) {
						return i;
					}
				}
			}
		}
		return -1;

	}

	private static void replaceString(List<Character> chars, int start, int end, String name, int number,
			Map<String, String> replacedStrs) {
		if (end == -1)
			return;
		// 从字符串里面截取字符串
		List<Character> subChars = chars.subList(start, end + 1);
		String text = Joiner.on("").join(subChars);
		replacedStrs.put(name + number, text);
		// 开始替换掉指定位置的字符
		for (int j = 0; j < end - start + 1; j++) {
			chars.remove(start);
		}
		// 重新创建
		subChars = new ArrayList<>();
		for (char c : (name + number + " ").toCharArray()) {
			subChars.add(c);
		}
		chars.addAll(start, subChars);
	}

}
