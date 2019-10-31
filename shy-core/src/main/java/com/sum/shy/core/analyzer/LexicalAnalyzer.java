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
	public static List<String> getWords(String line) {

//		System.out.println(line.trim());
		// 拆分的单元
		List<String> words = new ArrayList<>();
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
//				System.out.println(line);
			} else if (line.charAt(i) == '(') {
				line = LineUtils.replaceString(line, '(', ')', "invoke", count++, replacedStrs, true);
//				System.out.println(line);
			} else if (line.charAt(i) == '[') {
				line = LineUtils.replaceString(line, '[', ']', "array", count++, replacedStrs);
//				System.out.println(line);
			} else if (line.charAt(i) == '{') {
				line = LineUtils.replaceString(line, '{', '}', "map", count++, replacedStrs);
//				System.out.println(line);
			}
		}

		// 2.处理操作符,添加空格,方便后面的拆分
		for (String str : SYMBOLS) {
			line = line.replaceAll(str, " " + str + " ");
		}

		// 3.将多余的空格去掉
		line = LineUtils.removeSpace(line);
//		System.out.println(line);

		// 4.根据操作符,进行拆分
		words = new ArrayList<>(Arrays.asList(line.split(" ")));

		// 5.重新将替换的字符串替换回来
		for (int i = 0; i < words.size(); i++) {
			String str = replacedStrs.get(words.get(i));
			if (str != null) {
				words.set(i, str);
			}
		}
//		System.out.println(words);
//		System.out.println();

		return words;

	}

}
