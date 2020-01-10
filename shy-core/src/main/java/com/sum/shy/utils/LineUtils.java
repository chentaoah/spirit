package com.sum.shy.utils;

import java.util.ArrayList;
import java.util.List;

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

}
