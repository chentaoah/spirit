package com.sum.shy.utils;

/**
 * 字符串剪切工具
 * 
 * @author chentao26275
 *
 */
public class CuttingUtils {

	public static String getPrefix(String str) {
		String left = findLeft(str);
		if (left != null)
			return str.substring(0, str.indexOf(left));
		return null;
	}

	public static String getContent(String str) {
		String left = findLeft(str);
		String right = findRight(str);
		if (isSymmetric(left, right))
			return str.substring(str.indexOf(left) + 1, str.lastIndexOf(right));
		return null;
	}

	public static String findLeft(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '[') {
				return "[";
			} else if (c == '{') {
				return "{";
			} else if (c == '(') {
				return "(";
			} else if (c == '<') {
				return "<";
			} else if (c == '"') {// 如果发现字符串,则直接返回
				return null;
			}
		}
		return null;
	}

	public static String findRight(String str) {
		for (int i = str.length() - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if (c == ']') {
				return "]";
			} else if (c == '}') {
				return "}";
			} else if (c == ')') {
				return ")";
			} else if (c == '>') {
				return ">";
			} else if (c == '"') {// 如果发现字符串,则直接返回
				return null;
			}
		}
		return null;
	}

	public static boolean isSymmetric(String left, String right) {
		if ("[".equals(left) && "]".equals(right)) {
			return true;
		} else if ("{".equals(left) && "}".equals(right)) {
			return true;
		} else if ("(".equals(left) && ")".equals(right)) {
			return true;
		} else if ("<".equals(left) && ">".equals(right)) {
			return true;
		}
		return false;
	}

}
