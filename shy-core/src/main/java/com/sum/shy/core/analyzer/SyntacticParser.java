package com.sum.shy.core.analyzer;

import java.util.List;

import com.sum.shy.core.entity.Context;

/**
 * 语法分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class SyntacticParser {

	// 关键字
	public static final String[] KEYWORD = new String[] { "package", "import", "def", "class", "func" };

	public static String analysis(List<String> words) {
		// 判断首个单词是否关键字
		String str = words.get(0);
		for (String keyword : KEYWORD) {
			if (keyword.equals(str)) {
				return keyword;
			}
		}
		// 如果第二个语义是"=",那么可以认为是赋值语句
		str = words.get(1);
		if ("=".equals(str)) {
			String scope = Context.get().scope;
			switch (scope) {
			case "static":
				return "field";
			case "class":
				return "field";
			case "method":
				return "var";
			default:
				break;
			}
		}
		// 未知
		return null;
	}

}
