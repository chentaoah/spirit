package com.sum.shy.utils;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.pojo.element.Line;

public class LineUtils {

	public static List<Line> getSubLines(List<Line> lines, int index) {
		List<Line> list = new ArrayList<>();
		for (int i = index + 1, count = 1; i < lines.size(); i++) {
			String text = lines.get(i).text;
			boolean flag = false;// 是否进入"符号的范围内
			for (int j = 0; j < text.length(); j++) {
				char c = text.charAt(j);
				if (c == '"' && isEscaped(text, j)) // 判断是否进入了字符串中
					flag = !flag;
				if (!flag) {
					if (c == '{') {// 找到子域的结束符"}"
						count++;
					} else if (c == '}') {
						count--;
						if (count == 0)
							return list;
					}
				}
			}
			if (count == 0)
				break;
			list.add(lines.get(i));
		}
		return list;
	}

	public static boolean isEscaped(String text, int index) {
		int count = 0;
		for (int i = index - 1; i >= 0; i--) {
			if (text.charAt(index) == '\\') {
				count++;
			} else {
				break;
			}
		}
		return count % 2 == 0;
	}

	public static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_';
	}

	public static String mergeSpaces(String line) {
		line = line.trim();
		while (line.contains("  "))
			line = line.replaceAll("  ", " ");// 这里需要考虑一种情况，就是四个变两个，两个变一个
		return line;
	}

	public static String getSpaces(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++)
			sb.append(" ");
		return sb.toString();
	}

	public static String getIndent(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++)
			sb.append("\t");
		return sb.toString();
	}

}
