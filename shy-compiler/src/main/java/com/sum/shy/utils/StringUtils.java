package com.sum.shy.utils;

public class StringUtils {

	public static String getUnitByBrackets(String line, int index, String left, String right) {
		int start = -1;
		for (int j = index - 1; j >= 0; j--) {
			if (!Character.isLetter(line.charAt(j))) {
				start = j + 1;
			}
		}
		int end = -1;
		for (int j = index + 1, count = 1; j < line.length(); j++) {
			if (line.charAt(j) == '(') {
				count++;
			} else if (line.charAt(j) == ')') {
				count--;
			}
			if (count == 0) {
				end = j;
				break;
			}
		}
		return line.substring(start, end + 1);
	}
}
