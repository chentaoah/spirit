package com.sum.soon.utils;

public class LineUtils {

	public static boolean isNotEscaped(String text, int index) {
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
