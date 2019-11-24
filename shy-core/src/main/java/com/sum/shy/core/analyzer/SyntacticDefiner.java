package com.sum.shy.core.analyzer;

import java.util.List;

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
public class SyntacticDefiner {

	// 关键字
	public static final String[] KEYWORDS = new String[] { "package", "import", "def", "class", "func", "return", "if",
			"for", "while", "try" };

	public static String getSyntax(List<String> words) {
		try {
			if (words == null || words.size() == 0) {// 添加空校验
				return Constants.UNKNOWN;
			}

			String first = words.get(0);
			for (String keyword : KEYWORDS) {// 关键字语句
				if (keyword.equals(first)) {
					return keyword;
				}
			}

			if (words.size() == 1 && "}".equals(first)) {// 语句块的结束
				return Constants.END_SYNTAX;
			}
			if (words.size() == 1 && SemanticDelegate.isInvoke(first)) {// 单纯方法调用语句
				return Constants.INVOKE_SYNTAX;
			}
			if (words.size() == 1 && SemanticDelegate.isQuickIndex(first)) {// 单纯方法调用语句
				return Constants.INVOKE_SYNTAX;
			}
			if (words.size() == 2 && SemanticDelegate.isType(first)) {// 如果是类型,则是类型说明语句
				return Constants.DECLARE_SYNTAX;
			}

			String second = words.get(1);
			if ("=".equals(second)) {// 字段定义或者赋值语句
				return Constants.ASSIGN_SYNTAX;
			}

			String third = words.get(2);
			if ("}".equals(first)) {// else if语句
				if ("else".equals(second)) {
					if ("if".equals(third)) {
						return Constants.ELSEIF_SYNTAX;
					} else {
						return Constants.ELSE_SYNTAX;
					}
				}
			}
		} catch (Exception e) {
			// ignore
		}

		// 未知
		return Constants.UNKNOWN;
	}

}
