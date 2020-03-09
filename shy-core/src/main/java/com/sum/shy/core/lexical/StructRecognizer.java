package com.sum.shy.core.lexical;

import java.util.List;

import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;

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

	public static String getStructSyntax(List<Token> tokens) {
		// 第一个单词
		Token first = tokens.get(0);
		for (String keyword : STRUCT_KEYWORDS) {// 关键字语句
			if (keyword.equals(first.toString()))
				return keyword;
		}
		// 注解
		if (tokens.size() == 1 && first.isAnnotation())
			return Constants.ANNOTATION_SYNTAX;
		// 未知
		return null;
	}

}
