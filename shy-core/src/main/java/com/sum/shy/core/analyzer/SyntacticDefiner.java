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
			"while", "continue", "break", "try", "print", "debug", "sync" };

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

//			if ("if".equals(first)) {
//				for (String word : words) {
//					if (":".equals(word))
//						return Constants.IF_SINGLE_SYNTAX;
//				}
//				return Constants.IF_SYNTAX;
//			}

			if (words.size() == 1 && "}".equals(first)) {// 语句块的结束
				return Constants.END_SYNTAX;
			}
			if (words.size() == 1) {// 只有一个语素，一般来说都是invoke
				return Constants.INVOKE_SYNTAX;
			}
			if (words.size() == 2 && SemanticDelegate.isType(first)) {// 如果是类型,则是类型说明语句
				return Constants.DECLARE_SYNTAX;
			}

			String second = words.get(1);
			if ("=".equals(second)) {// 字段定义或者赋值语句
				return Constants.ASSIGN_SYNTAX;
			}
			if ("<<".equals(second)) {
				return Constants.FAST_ADD_SYNTAX;
			}
			if ("?".equals(second)) {
				return Constants.JUDGE_INVOKE_SYNTAX;
			}

			String third = words.get(2);
			if ("for".equals(first)) {
				if ("in".equals(third)) {
					return Constants.FOR_IN_SYNTAX;
				}
				return Constants.FOR_SYNTAX;
			}

			if ("}".equals(first)) {// else if语句
				if ("else".equals(second)) {
					if ("if".equals(third)) {
						return Constants.ELSEIF_SYNTAX;
					} else {
						return Constants.ELSE_SYNTAX;
					}
				} else if ("catch".equals(second)) {
					return Constants.CATCH_SYNTAX;
				}
			}
		} catch (Exception e) {
			// ignore
		}

		// 未知
		return Constants.UNKNOWN;
	}

}
