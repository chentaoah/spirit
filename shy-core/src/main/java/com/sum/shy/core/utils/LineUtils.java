package com.sum.shy.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.document.Line;

public class LineUtils {

	// 返回子块
	public static List<Line> getSubLines(List<Line> lines, int index) {
		List<Line> list = new ArrayList<>();
		for (int i = index + 1, count = 1; i < lines.size(); i++) {
			String text = lines.get(i).text;
			boolean flag = false;// 是否进入"符号的范围内
			for (int j = 0; j < text.length(); j++) {
				char c = text.charAt(j);
				if (c == '"' && isBoundary(text, j)) // 判断是否进入了字符串中
					flag = !flag;
				if (!flag) {
					if (c == '{') {// 找到子域的结束符"}"
						count++;
					} else if (c == '}') {
						count--;
					}
				}
			}
			if (count == 0)
				break;
			list.add(lines.get(i));
		}
		return list;
	}

	public static boolean isBoundary(String text, int i) {
		int count = 0;
		while (--i >= 0) {
			if (text.charAt(i) == '\\') {
				count++;
			} else {
				break;
			}
		}
		return count % 2 == 0;
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

}
