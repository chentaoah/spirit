package com.sum.spirit.utils;

public class LineUtils {

	public static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_';
	}

	public static String mergeSpaces(String line) {
		line = line.trim();
		while (line.contains("  ")) {
			line = line.replaceAll("  ", " ");// 这里需要考虑一种情况，就是四个变两个，两个变一个
		}
		return line;
	}

	public static String getSpaces(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String getIndent(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	public static char flipChar(char c) {
		if (c == '(') {
			return ')';
		} else if (c == '{') {
			return '}';
		} else if (c == '[') {
			return ']';
		} else if (c == '<') {
			return '>';
		} else if (c == '"') {
			return '"';
		} else if (c == '\'') {
			return '\'';
		} else if (c == '|') {
			return '|';
		}
		return c;
	}

	public static boolean isNotEscaped(CharSequence chars, int index) {
		int count = 0;
		for (int i = index - 1; i >= 0; i--) {
			if (chars.charAt(index) == '\\') {
				count++;
			} else {
				break;
			}
		}
		return count % 2 == 0;
	}

	public static int findEndIndex(CharSequence chars, int start, char left, char right) {
		boolean flag = false;
		for (int index = start, count = 0; index < chars.length(); index++) {
			char c = chars.charAt(index);
			if (c == '"' && isNotEscaped(chars, index)) {// 如果是“"”符号，并且没有被转义
				flag = !flag;
				if (!flag && right == '"') {
					return index;
				}
			}
			if (!flag) {
				if (count % 2 == 0) {// 防止完全一样的分隔符|xxx|
					if (c == left) {
						count++;
					} else if (c == right) {
						count--;
						if (count == 0) {
							return index;
						}
					}
				} else {
					if (c == right) {
						count--;
						if (count == 0) {
							return index;
						}
					} else if (c == left) {
						count++;
					}
				}
			}
		}
		return -1;
	}

}
