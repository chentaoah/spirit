package com.sum.shy.core.lexical;

import java.util.List;

/**
 * 语法分析器
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年10月29日
 */
public class StructRecognizer {
	// 结构体关键字,组成一个类的基本结构
	public static final String[] STRUCT_KEYWORDS = new String[] { "package", "import", "interface", "abstract", "class",
			"func" };

	public static String getStructSyntax(List<String> words) {
		// 第一个单词
		String first = words.get(0);
		for (String keyword : STRUCT_KEYWORDS) {// 关键字语句
			if (keyword.equals(first))
				return keyword;
		}
		// 未知
		return null;
	}

}
