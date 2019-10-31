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
	public static final String[] KEYWORDS = new String[] { "package", "import", "def", "class", "func", "return",
			"if" };

	public static String getSyntax(List<String> words) {
		
		String first = words.get(0);
		for (String keyword : KEYWORDS) {// 关键字语句
			if (keyword.equals(first)) {
				return keyword;
			}
		}

		if (words.size() == 1 && "}".equals(first)) {// 语句块的结束
			return "end";
		}
		if (words.size() == 1 && SemanticDelegate.isInvoke(first)) {// 单纯方法调用语句
			return "invoke";
		}

		String second = words.get(1);
		if ("=".equals(second)) {// 字段定义或者赋值语句
			String scope = Context.get().scope;
			switch (scope) {
			case "static":
				return "field";
			case "class":
				return "field";
			case "method":
				return "assignment";
			default:
				break;
			}
		}

		String third = words.get(2);
		if ("}".equals(first)) {// else if语句
			if ("else".equals(second)) {
				if ("if".equals(third)) {
					return "elseif";
				}
			}
		}

		// 未知
		return null;
	}

}
