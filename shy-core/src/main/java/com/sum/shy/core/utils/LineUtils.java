package com.sum.shy.core.utils;

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

	public static String replaceString(String line, char left, char right, String name, int number,
			Map<String, String> map) {
		return replaceString(line, left, right, name, number, map, false, false);
	}

	public static String replaceString(String line, char left, char right, String name, int number,
			Map<String, String> map, boolean aleft) {
		return replaceString(line, left, right, name, number, map, false, aleft);
	}

	public static String replaceString(String line, char left, char right, String name, int number,
			Map<String, String> map, boolean greed, boolean aleft) {
		// 先统计一下索引位置
		List<Pair> list = new ArrayList<>();
		// 是否进入"符号的范围内
		boolean flag = false;
		for (int i = 0, start = -1, count = 0; i < line.length(); i++) {
			// 如果进入了"符号的范围,并且left和right不是",则直接跳过
			if (line.charAt(i) == '"' && line.charAt(i - 1 >= 0 ? i - 1 : i) != '\\') {
				flag = flag ? false : true;
			}
			if (flag && left != '"' && right != '"') {
				continue;
			}
			// 小心这里left和right是一样的
			if (line.charAt(i) == left && count % 2 == 0) {
				count++;
				if (count == 1) {
					start = i;// 让start尽量留在最左边
				}
				if (aleft) {
					// what like user.say()
					for (int j = i - 1; j >= 0; j--) {
						char c = line.charAt(j);
						if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '.') {
							// 符合条件，但是已经到最边界了，那么则以这个边界为准
							if (j == 0) {
								start = j;
								break;
							}
							continue;
						} else {
							start = j + 1;
							break;
						}
					}
				}
			} else if (line.charAt(i) == right && line.charAt(i - 1 >= 0 ? i - 1 : i) != '\\') {// 排除转义的可能
				count--;
				if (count == 0) {
					if (start >= 0 && i > start) {
						list.add(new Pair(start, i));
						start = -1;
						if (!greed) {
							break;
						}
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
		int count = number;
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