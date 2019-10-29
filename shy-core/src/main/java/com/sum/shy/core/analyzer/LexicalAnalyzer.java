package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.utils.LineUtils;

/**
 * 词法分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class LexicalAnalyzer {

	// 操作符
	public static final String[] SYMBOLS = new String[] { "==", "!=", "<=", ">=", "&&", "[|]{2}", "=", "\\+", "-",
			"\\*", "/", "%", "<", ">", "\\[", "\\]", "\\{", "\\}", "\\(", "\\)", "\\:", "," };

	/**
	 * 将语句拆分成一个一个单元
	 * 
	 * @param line
	 * @return
	 */
	public static List<String> analysis(String line) {

		System.out.println(line.trim());
		// 拆分的单元
		List<String> units = new ArrayList<>();
		// 替换的字符串
		Map<String, String> replacedStrs = new HashMap<>();

		// 去掉前后的空格
		line = line.trim();

		// 1.将字符串,方法调用,数组,键值对,都当做一个整体来对待
		// 这里需要解决一个括号谁套谁的问题
		int count = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '"') {
				line = LineUtils.replaceString(line, '"', '"', "str", count++, replacedStrs);
				System.out.println(line);
			} else if (line.charAt(i) == '(') {
				line = LineUtils.replaceString(line, '(', ')', "invoke", count++, replacedStrs, true);
				System.out.println(line);
			} else if (line.charAt(i) == '[') {
				line = LineUtils.replaceString(line, '[', ']', "array", count++, replacedStrs);
				System.out.println(line);
			} else if (line.charAt(i) == '{') {
				line = LineUtils.replaceString(line, '{', '}', "map", count++, replacedStrs);
				System.out.println(line);
			}
		}

		// 2.处理操作符,添加空格,方便后面的拆分
		for (String str : SYMBOLS) {
			line = line.replaceAll(str, " " + str + " ");
		}

		// 3.将多余的空格去掉
		line = LineUtils.removeSpace(line);
		System.out.println(line);

		// 4.根据操作符,进行拆分
		units = new ArrayList<>(Arrays.asList(line.split(" ")));

		// 5.重新将替换的字符串替换回来
		for (int i = 0; i < units.size(); i++) {
			String str = replacedStrs.get(units.get(i));
			if (str != null) {
				units.set(i, str);
			}
		}
		System.out.println(units);

		return units;

	}

	/**
	 * 将单元进行拆分
	 * 
	 * @param line
	 * @param unit
	 * @return
	 */
	public static List<String> analysisUnit(String type, String unit) {
		if ("array".equals(type)) {// 如果是数组,则解析子语句
			String str = unit.substring(1, unit.length() - 1);
			List<String> units = analysis(str);
			units.add(0, "[");
			units.add(units.size() - 1, "]");
			return units;

		} else if ("map".equals(type)) {
			String str = unit.substring(1, unit.length() - 1);
			List<String> units = analysis(str);
			units.add(0, "{");
			units.add(units.size() - 1, "}");
			return units;

		} else if (type.startsWith("invoke_")) {
			String name = unit.substring(0, unit.indexOf("("));
			String str = unit.substring(unit.indexOf("(") + 1, unit.lastIndexOf(")"));
			List<String> units = analysis(str);
			units.add(0, name);
			units.add(1, "(");
			units.add(units.size() - 1, ")");
			return units;

		}
		return null;
	}

}
