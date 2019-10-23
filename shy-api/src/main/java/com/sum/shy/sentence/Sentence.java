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
	// 语素
	public List<Morpheme> morphemes = new ArrayList<>();

	public Sentence(String line) {
		this.line = line;

		// 替换的字符串
		Map<String, String> replacedStrs = new HashMap<>();
		List<String> units = new ArrayList<>();

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

		// 3.进行整体替换
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

		System.out.println(replacedStrs);

		// 4.处理操作符,添加空格,方便后面的拆分
		line = processSymbols(line);
		System.out.println(line);

		// 5.将多余的空格去掉
		line = LineUtils.removeSpace(line);
		System.out.println(line);

		// 6.根据操作符,进行拆分
		splitString(line, units);
		System.out.println(units);

		// 7.重新将替换的字符串替换回来
		rereplaceString(replacedStrs, units);
		System.out.println(units);

		// 8.将拆分的单元,转化成语素
		createMorpheme(units);

	}

	private String processSymbols(String line) {
		for (String str : SYMBOLS) {
			line = line.replaceAll(str, " " + str + " ");
		}
		return line;
	}

	private void splitString(String line, List<String> units) {
		for (String str : line.split(" ")) {
			units.add(str);
		}
	}

	private void rereplaceString(Map<String, String> replacedStrs, List<String> units) {
		for (int i = 0; i < units.size(); i++) {
			String str = replacedStrs.get(units.get(i));
			if (str != null) {
				units.set(i, str);
			}
		}
	}

	private void createMorpheme(List<String> units) {
		for (int i = 0; i < units.size(); i++) {
			morphemes.add(Morpheme.create(units.get(i)));
		}
	}

	// 获取单元
	public Morpheme getMorpheme(int index) {
		return morphemes.get(index);
	}

	// 获取语素的字符串
	public String getStr(int index) {
		return getMorpheme(index).str;
	}

	public String getKeyword(String scope) {
		// 判断首个单词是否关键字
		String str = getStr(0);
		for (String keyword : KEYWORD) {
			if (keyword.equals(str)) {
				return keyword;
			}
		}
		// 如果第二个语义是"=",那么可以认为是赋值语句
		str = getStr(1);
		if ("=".equals(str)) {
			return "method".equals(scope) ? "var" : "field";
		}
		// 如果只有右值的话,则只能返回调用
		return "invoke";

	}

}
