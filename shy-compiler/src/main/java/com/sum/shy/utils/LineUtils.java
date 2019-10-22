package com.sum.shy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public static String replaceString(String line, char left, char right, String name, Map<String, String> map) {
		return replaceString(line, left, right, name, map, false);
	}

	public static String replaceString(String line, char left, char right, String name, Map<String, String> map,
			boolean aleft) {
		// 先统计一下索引位置
		List<Pair> list = new ArrayList<>();
		for (int i = 0, start = -1, count = 0; i < line.length(); i++) {
			// 小心这里left和right是一样的
			if (line.charAt(i) == left && count % 2 == 0) {
				count++;
				if (count == 1) {
					start = i;// 让start尽量留在最左边
				}
				if (aleft) {
					// what like user.say()
					for (int j = i - 1; j >= 0; j--) {
						if (!(Character.isLetter(line.charAt(j)) || line.charAt(j) == '.')) {
							start = j + 1;
						}
					}
				}
			} else if (line.charAt(i) == right && line.charAt(i - 1 >= 0 ? i - 1 : i) != '\\') {// 排除转义的可能
				count--;
				if (count == 0) {
					if (start >= 0 && i > start) {
						list.add(new Pair(start, i));
						start = -1;
					}
				}
			}

		}
		// 把所有需要被替换的字符串截取出来
		List<String> subStrs = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Pair pair = list.get(i);
			// 截取字符串
			String str = line.substring(pair.start, pair.end + 1);
			subStrs.add(str);
		}
		// 开始替换字符串
		int count = 0;
		for (String str : subStrs) {
			String key = "$" + name + count++;
			line = line.replace(str, key);
			if (map != null)
				map.put(key, str);
		}

		return line;
	}

	public static class Pair {
		public int start;
		public int end;

		public Pair(int start, int end) {
			this.start = start;
			this.end = end;
		}

	}

}
