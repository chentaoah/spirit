package com.sum.shy.sentence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.sum.shy.utils.LineUtils;

public class Sentence {

	// 操作符
	public static final String[] SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "[|]{2}", "=", "\\+", "-",
			"\\*", "/", "%", "<", ">", "\\{", "\\}" };
	// 关键字
	public static final String[] KEYWORD = new String[] { "package", "import", "def", "class", "func" };
	// 数组正则
	public static final Pattern ARRAY_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[ ]*=[ ]*\\[[\\s\\S]+\\]$");
	// 键值对正则
	public static final Pattern MAP_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[ ]*=[ ]*\\{[\\s\\S]+\\}$");

	// 一行
	public String line;
	// 单元
	public List<String> units = new ArrayList<>();

	public Sentence(String line) {
		this.line = line;

		System.out.println(line.trim());

		// 替换的字符串
		Map<String, String> replacedStrs = new HashMap<>();

		// 去掉前后的空格
		line = line.trim();

		// 1.将字符串,方法调用,数组,键值对,都当做一个整体来对待
		// 这里需要解决一个括号谁套谁的问题
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '"') {
				line = LineUtils.replaceString(line, '"', '"', "str", replacedStrs);
				System.out.println(line);
			} else if (line.charAt(i) == '(') {
				line = LineUtils.replaceString(line, '(', ')', "invoke", replacedStrs, true);
				System.out.println(line);
			} else if (line.charAt(i) == '[') {
				line = LineUtils.replaceString(line, '[', ']', "array", replacedStrs);
				System.out.println(line);
			} else if (line.charAt(i) == '{') {
				line = LineUtils.replaceString(line, '{', '}', "map", replacedStrs);
				System.out.println(line);
			}
		}

		// 2.处理操作符,添加空格,方便后面的拆分
		line = processSymbols(line);

		// 3.将多余的空格去掉
		line = LineUtils.removeSpace(line);
		System.out.println(line);

		// 4.根据操作符,进行拆分
		splitString(line);

		// 5.重新将替换的字符串替换回来
		rereplaceString(replacedStrs);
		System.out.println(units);
		System.out.println("");// 换行

	}

	private String processSymbols(String line) {
		for (String str : SYMBOLS) {
			line = line.replaceAll(str, " " + str + " ");
		}
		return line;
	}

	private void splitString(String line) {
		for (String str : line.split(" ")) {
			units.add(str);
		}
	}

	private void rereplaceString(Map<String, String> replacedStrs) {
		for (int i = 0; i < units.size(); i++) {
			String str = replacedStrs.get(units.get(i));
			if (str != null) {
				units.set(i, str);
			}
		}
	}

	// 获取字符串
	public String getUnit(int index) {
		return units.get(index);
	}

	// 这里的scope只有可能是static和class
	public String getCommand(String scope) {
		// 判断首个单词是否关键字
		String str = getUnit(0);
		for (String keyword : KEYWORD) {
			if (keyword.equals(str)) {
				return keyword;
			}
		}
		// 如果第二个语义是"=",那么可以认为是赋值语句
		str = getUnit(1);
		if ("=".equals(str)) {
			return "field";
		}
		// 未知
		return null;

	}

	@Override
	public String toString() {
		return "Sentence " + units;
	}

}
