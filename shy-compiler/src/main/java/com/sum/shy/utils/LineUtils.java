package com.sum.shy.utils;

import java.util.ArrayList;
import java.util.List;

public class LineUtils {

	// 返回子块
	public static List<String> getSubLines(List<String> lines, int index) {
		// 找到子域的结束符"}"
		List<String> list = new ArrayList<>();
		for (int i = index + 1, count = 1; i < lines.size(); i++) {
			String line = lines.get(i);
			// 遍历
			for (int j = 0; j < line.length(); j++) {
				if (line.charAt(j) == '{') {
					count++;
				} else if (line.charAt(j) == '}') {
					count--;
				}
			}
			if (count == 0) {
				break;
			}
			list.add(line);
		}
		return lines;
	}

	// 去掉多余的空格
	public static String removeSpace(String line) {
		// 去掉首尾
		line = line.trim();
		while (line.contains("  ")) {
			line.replaceAll("  ", "");
		}
		return line;
	}

}
