package com.sum.shy.entity;

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

	// 替换的字符串
	public Map<String, String> replacedStrs = new HashMap<>();

	// 拆分的语义单元
	public List<String> units = new ArrayList<>();

	public Sentence(String line) {
		this.line = line;

		// 去掉前后的空格
		line = line.trim();

		// 1.将字符串,方法调用,数组,键值对,都当做一个整体来对待
		if (line.contains("\"")) {
			line = LineUtils.replaceString(line, '"', '"', "str", replacedStrs);
			System.out.println(line);
		}

		// 2.将多余的空格去掉
		line = LineUtils.removeSpace(line);
		System.out.println(line);

		if (line.contains("(")) {
			line = LineUtils.replaceString(line, '(', ')', "invoke", replacedStrs, true);
			System.out.println(line);
		}

		if (ARRAY_PATTERN.matcher(line).matches()) {
			line = LineUtils.replaceString(line, '[', ']', "array", replacedStrs);
			System.out.println(line);
		}

		if (MAP_PATTERN.matcher(line).matches()) {
			line = LineUtils.replaceString(line, '{', '}', "map", replacedStrs);
			System.out.println(line);
		}

		// 3.处理操作符,添加空格,方便后面的拆分
		line = processSymbols(line);
		System.out.println(line);

		// 4.将多余的空格去掉
		line = LineUtils.removeSpace(line);
		System.out.println(line);

		// 5.根据操作符,进行拆分
		splitString(line);
		System.out.println(units);
		System.out.println(replacedStrs);

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

	// 获取单元
	public String getUnit(int index) {
		return index > units.size() - 1 ? null : units.get(index);
	}

	// 获取被替换的字符串
	public String getReplacedStr(int index) {
		return index > units.size() - 1 ? null : replacedStrs.get(units.get(index));
	}

	// 获取被替换的字符串
	public String getReplacedStr(String str) {
		return replacedStrs.get(str);
	}

	public String getKeyword(String scope) {
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
			return "method".equals(scope) ? "var" : "field";
		}
		// 如果只有右值的话,则只能返回调用
		return "invoke";

	}

}
